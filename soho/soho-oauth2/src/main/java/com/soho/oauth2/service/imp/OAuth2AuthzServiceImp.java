package com.soho.oauth2.service.imp;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2ErrorCode;
import com.soho.oauth2.model.OAuth2Token;
import com.soho.oauth2.service.OAuth2AuthzService;
import com.soho.oauth2.service.OAuth2TokenService;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.security.EncryptService;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.spring.utils.HttpUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by shadow on 2017/12/18.
 */
@Service
public class OAuth2AuthzServiceImp implements OAuth2AuthzService {

    @Autowired(required = false)
    private OAuth2TokenService oAuth2TokenService;
    @Autowired(required = false)
    private EncryptService encryptService;

    private static final String CLIENT_PBK = "client_pbk";

    @Override
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        String redirect_uri = null;
        try {
            // 构建OAuth 授权请求
            OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            // 获取客户端重定向地址
            redirect_uri = oAuthAuthzRequest.getRedirectURI();
            // 检查传入的客户端参数
            oAuth2TokenService.validState(oAuthAuthzRequest.getState());
            oAuth2TokenService.validResponseType(oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE));
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(oAuthAuthzRequest.getClientId(), null);
            oAuth2TokenService.validRedirectUri(oAuth2Client, redirect_uri);
            String username = StringUtils.isEmpty(oAuthAuthzRequest.getParam("username")) ? "" : oAuthAuthzRequest.getParam("username");
            String password = StringUtils.isEmpty(oAuthAuthzRequest.getParam("password")) ? "" : oAuthAuthzRequest.getParam("password");
            String error = StringUtils.isEmpty(oAuthAuthzRequest.getParam("error")) ? "" : oAuthAuthzRequest.getParam("error");
            // 校验PBK签名
            Object s_pbk = SessionUtils.getAttribute(CLIENT_PBK);
            String pbk = oAuthAuthzRequest.getParam(CLIENT_PBK);
            if (StringUtils.isEmpty(s_pbk) || !s_pbk.equals(pbk)) {
                return toInitLoginView(oAuthAuthzRequest, redirect_uri, username, error);
            }
            OAuth2Token oAuth2Token = null;
            try {
                oAuth2TokenService.validJaqState();
                Map<String, Object> loginInfo = new FastMap<>().add("username", username).add("password", password).done();
                oAuth2Token = oAuth2TokenService.loginByUsername(loginInfo);
                OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                oAuth2Token.setClient_id(oAuthAuthzRequest.getClientId());
                oAuth2Token.setCode(oAuthIssuer.authorizationCode());
                oAuth2Token.setAccess_token(oAuthIssuer.accessToken());
                oAuth2Token.setRefresh_token(oAuthIssuer.accessToken());
                oAuth2Token.setRedirect_uri(redirect_uri);
                oAuth2Token = oAuth2TokenService.addClientToken(oAuth2Token);
                oAuth2TokenService.delJaqState();
            } catch (BizErrorEx ex) { // WEB方式登录失败时跳转到登陆页面
                return toFailureLoginView(oAuthAuthzRequest, oAuth2TokenService.getOAuth2DomainUri() + request.getRequestURI(), redirect_uri, username, ex.getMessage());
            }
            // 进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);
            // 设置授权码
            builder.setCode(oAuth2Token.getCode());
            // 设置返回客户端自定义数据
            builder.setParam("state", oAuthAuthzRequest.getState());
            // 构建响应
            final OAuthResponse oAuthResponse = builder.location(redirect_uri).buildQueryMessage();
            // 根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = buildHttpUtf8Header();
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (OAuthProblemException e) { //返回错误消息（如?error=）
            if (StringUtils.isEmpty(redirect_uri)) {
                return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
            }
            try {
                final OAuthResponse oAuthResponse = OAuthASResponse.errorResponse(HttpServletResponse.SC_FOUND)
                        .error(e).location(redirect_uri).buildQueryMessage();
                HttpHeaders headers = buildHttpUtf8Header();
                headers.setLocation(new URI(oAuthResponse.getLocationUri()));
                return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求失败", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 构建OAuth请求
            OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);
            // 校验类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            oAuth2TokenService.validGrantType(oAuthTokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE));
            // 校验客户端id是否正确
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(oAuthTokenRequest.getClientId(), getClientIpAddr(request));
            // 校验客户端重定向参数
            oAuth2TokenService.validRedirectUri(oAuth2Client, oAuthTokenRequest.getRedirectURI());
            // 校验客户端密钥参数
            oAuth2TokenService.validClientSecret(oAuth2Client, oAuthTokenRequest.getClientSecret());
            // 获取授权码并进行认证
            OAuth2Token oAuth2Token = oAuth2TokenService.getAccessTokenByCode(oAuth2Client.getClient_id(), oAuthTokenRequest.getParam(OAuth.OAUTH_CODE), oAuthTokenRequest.getRedirectURI());
            // 生成校验的PBK
            String access_pbk = oAuth2TokenService.buildAccessPbk(oAuth2Client.getClient_id(), oAuth2Token.getAccess_time(), oAuth2Token.getAccess_token());
            // 生成OAuth响应
            return new FastMap()
                    .add("access_token", oAuth2Token.getAccess_token())
                    .add("access_pbk", access_pbk)
                    .add("access_time", oAuth2Token.getAccess_time())
                    .add("refresh_token", oAuth2Token.getRefresh_token())
                    .add("refresh_time", oAuth2Token.getRefresh_time())
                    .add("token_expire", oAuth2Token.getToken_expire())
                    .done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (OAuthProblemException e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Access Token
            String accessToken = oAuthAccessResourceRequest.getAccessToken();
            // 获取令牌公钥
            String accessPbk = request.getParameter("access_pbk");
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(accessToken, accessPbk);
            // 校验客户端配置
            oAuth2TokenService.validOAuth2Client(oAuth2Token.getClient_id(), getClientIpAddr(request));
            // 读取OAuth用户信息,生成OAuth响应
            return oAuth2TokenService.getOAuthUser(oAuth2Token.getUid());
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (OAuthProblemException e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Access_Token
            String access_token = oAuthAccessResourceRequest.getAccessToken();
            // 获取Client_Id
            String client_id = request.getParameter("client_id");
            // 获取Client_Secret
            String client_secret = request.getParameter("client_secret");
            // 获取Refresh_Token
            String refresh_token = request.getParameter("refresh_token");
            // 获取令牌公钥
            String access_pbk = request.getParameter("access_pbk");
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(access_token, access_pbk, false);
            // 校验客户端配置
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(oAuth2Token.getClient_id(), getClientIpAddr(request));
            // 校验客户端密钥
            oAuth2TokenService.validClientSecret(oAuth2Client, client_secret);
            // 延期Access_Token授权时间
            oAuth2Token = oAuth2TokenService.refreshToken(access_token, refresh_token);
            return new FastMap()
                    .add("result", "令牌续期成功")
                    .add("client_id", oAuth2Token.getClient_id())
                    .add("access_token", oAuth2Token.getAccess_token())
                    .add("refresh_time", oAuth2Token.getRefresh_time())
                    .add("token_expire", oAuth2Token.getToken_expire()).done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (OAuthProblemException e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Object logout_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Access Token
            String access_token = oAuthAccessResourceRequest.getAccessToken();
            // 获取令牌公钥
            String access_pbk = request.getParameter("access_pbk");
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(access_token, access_pbk);
            // 校验客户端配置
            oAuth2TokenService.validOAuth2Client(oAuth2Token.getClient_id(), getClientIpAddr(request));
            // 注销Access Token
            oAuth2TokenService.logoutToken(oAuth2Token);
            // 根据OAuthResponse生成ResponseEntity
            return new FastMap().add("result", "令牌注销成功").done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (OAuthProblemException e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // 登录失败重定向初始化界面
    private ModelAndView toFailureLoginView(OAuthAuthzRequest oAuthAuthzRequest, String req_uri, String redirect_uri, String username, String error) {
        try {
            StringBuffer buffer = new StringBuffer("redirect:").append(req_uri);
            buffer.append("?client_id=").append(oAuthAuthzRequest.getClientId());
            buffer.append("&response_type=").append(oAuthAuthzRequest.getResponseType());
            buffer.append("&redirect_uri=").append(URLEncoder.encode(redirect_uri, "UTF-8"));
            buffer.append("&state=").append(oAuthAuthzRequest.getState());
            buffer.append("&error=").append(URLEncoder.encode(error, "UTF-8"));
            buffer.append("&username=").append(username);
            return new FastView(buffer.toString()).done();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 初始化OAUTH2.0登录界面
    private ModelAndView toInitLoginView(OAuthAuthzRequest oAuthAuthzRequest, String redirect_uri, String username, String error) {
        OAuth2Client client = new OAuth2Client();
        client.setClient_id(oAuthAuthzRequest.getClientId());
        client.setResponse_type(oAuthAuthzRequest.getResponseType());
        client.setRedirect_uri(redirect_uri);
        client.setState(oAuthAuthzRequest.getState());
        String pbk = encryptService.md5(client.getClient_id() + System.currentTimeMillis());
        SessionUtils.setAttribute(CLIENT_PBK, pbk);
        return new FastView(oAuth2TokenService.getOAuth2LoginView())
                .add("client", client)
                .add("username", username)
                .add("error", error)
                .add(CLIENT_PBK, pbk)
                .done();
    }

    // 输出失败异常JSON数据对象
    private Object responseFailEntity(String errorCode, String errorMsg, int status) throws BizErrorEx {
        throw new BizErrorEx(errorCode, errorMsg, HttpStatus.valueOf(status));
    }

    private HttpHeaders buildHttpUtf8Header() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "text/html;charset=UTF-8");
        return headers;
    }

    private String getClientIpAddr(HttpServletRequest request) {
        return HttpUtils.getIpAddr(request);
    }

}
