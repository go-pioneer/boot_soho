package com.soho.oauth2.service.imp;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2ClientParam;
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
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
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
    private static final String ACCESS_PBK = "access_pbk";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String ERROR = "error";

    @Override
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        // 构建OAuth请求参数对象
        OAuth2ClientParam parameter = buildAuthorizeParam(request);
        try {
            // 检查传入的客户端参数
            oAuth2TokenService.validState(parameter.getState());
            oAuth2TokenService.validResponseType(parameter.getResponse_type());
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(parameter.getClient_id(), null);
            oAuth2TokenService.validRedirectUri(oAuth2Client, parameter.getRedirect_uri());
            // 校验PBK签名
            Object client_pbk = SessionUtils.getAttribute(CLIENT_PBK);
            if (StringUtils.isEmpty(client_pbk) || !client_pbk.equals(request.getParameter(CLIENT_PBK))) {
                return toInitLoginView(parameter);
            }
            OAuth2Token oAuth2Token = null;
            try {
                oAuth2TokenService.validJaqState();
                Map<String, Object> loginInfo = new FastMap<>().add("username", parameter.getUsername()).add("password", parameter.getPassword()).done();
                oAuth2Token = oAuth2TokenService.loginByUsername(loginInfo);
                OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                oAuth2Token.setClient_id(oAuth2Client.getClient_id());
                oAuth2Token.setCode(oAuthIssuer.authorizationCode());
                oAuth2Token.setAccess_token(oAuthIssuer.accessToken());
                oAuth2Token.setRefresh_token(oAuthIssuer.accessToken());
                oAuth2Token.setRedirect_uri(parameter.getRedirect_uri());
                oAuth2Token = oAuth2TokenService.addClientToken(oAuth2Token);
                oAuth2TokenService.delJaqState();
            } catch (BizErrorEx ex) { // WEB方式登录失败时跳转到登陆页面
                parameter.setError(ex.getMessage());
                return toFailureLoginView(parameter, oAuth2TokenService.getOAuth2DomainUri() + request.getRequestURI());
            }
            // 进行OAuth响应构建
            OAuthASResponse.OAuthAuthorizationResponseBuilder builder =
                    OAuthASResponse.authorizationResponse(request,
                            HttpServletResponse.SC_FOUND);
            // 设置授权码
            builder.setCode(oAuth2Token.getCode());
            // 设置返回客户端自定义数据
            builder.setParam("state", parameter.getState());
            // 构建响应
            final OAuthResponse oAuthResponse = builder.location(parameter.getRedirect_uri()).buildQueryMessage();
            // 根据OAuthResponse返回ResponseEntity响应
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "text/html;charset=UTF-8");
            headers.setLocation(new URI(oAuthResponse.getLocationUri()));
            return new ResponseEntity(headers, HttpStatus.valueOf(oAuthResponse.getResponseStatus()));
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            String client_id = request.getParameter(OAuth.OAUTH_CLIENT_ID);
            String client_secret = request.getParameter(OAuth.OAUTH_CLIENT_SECRET);
            String redirect_uri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
            String code = request.getParameter(OAuth.OAUTH_CODE);
            // 校验类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            oAuth2TokenService.validGrantType(request.getParameter(OAuth.OAUTH_GRANT_TYPE));
            // 校验客户端id是否正确
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(client_id, getIpAddr(request));
            // 校验客户端重定向参数
            oAuth2TokenService.validRedirectUri(oAuth2Client, redirect_uri);
            // 校验客户端密钥参数
            oAuth2TokenService.validClientSecret(oAuth2Client, client_secret);
            // 获取授权码并进行认证
            OAuth2Token oAuth2Token = oAuth2TokenService.getAccessTokenByCode(client_id, code, redirect_uri);
            // 生成校验的PBK
            String access_pbk = oAuth2TokenService.buildAccessPbk(oAuth2Token.getAccess_token());
            // 生成OAuth响应
            return new FastMap()
                    .add("access_token", oAuth2Token.getAccess_token())
                    .add("refresh_token", oAuth2Token.getRefresh_token())
                    .add("access_pbk", access_pbk)
                    .add("access_time", oAuth2Token.getAccess_time())
                    .add("refresh_time", oAuth2Token.getRefresh_time())
                    .add("token_expire", oAuth2Token.getToken_expire())
                    .done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(request.getParameter(OAuth.OAUTH_ACCESS_TOKEN), request.getParameter(ACCESS_PBK));
            // 校验客户端配置
            oAuth2TokenService.validOAuth2Client(oAuth2Token.getClient_id(), getIpAddr(request));
            // 读取OAuth用户信息,生成OAuth响应
            return oAuth2TokenService.getOAuthUser(oAuth2Token.getUid());
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 构建OAuth资源请求
            String client_id = request.getParameter(OAuth.OAUTH_CLIENT_ID);
            String client_secret = request.getParameter(OAuth.OAUTH_CLIENT_SECRET);
            String refresh_token = request.getParameter(OAuth.OAUTH_REFRESH_TOKEN);
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(request.getParameter(OAuth.OAUTH_ACCESS_TOKEN), request.getParameter(ACCESS_PBK), false);
            // 校验客户端配置
            OAuth2Client oAuth2Client = oAuth2TokenService.validOAuth2Client(client_id, getIpAddr(request));
            // 校验客户端密钥
            oAuth2TokenService.validClientSecret(oAuth2Client, client_secret);
            // 延期Access_Token授权时间
            oAuth2TokenService.refreshToken(oAuth2Token.getAccess_token(), refresh_token);
            return new FastMap()
                    .add("result", "令牌续期成功")
                    .add("client_id", oAuth2Token.getClient_id())
                    .add("access_token", oAuth2Token.getAccess_token())
                    .add("refresh_time", oAuth2Token.getRefresh_time())
                    .add("token_expire", oAuth2Token.getToken_expire()).done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public Object logout_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            // 校验Token,Pbk
            OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(request.getParameter(OAuth.OAUTH_ACCESS_TOKEN), request.getParameter(ACCESS_PBK));
            // 校验客户端配置
            oAuth2TokenService.validOAuth2Client(oAuth2Token.getClient_id(), getIpAddr(request));
            // 注销Access Token
            oAuth2TokenService.logoutToken(oAuth2Token);
            // 根据OAuthResponse生成ResponseEntity
            return new FastMap().add("result", "令牌注销成功").done();
        } catch (BizErrorEx e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        }
    }

    // 登录失败重定向初始化界面
    private ModelAndView toFailureLoginView(OAuth2ClientParam parameter, String req_uri) {
        try {
            StringBuffer buffer = new StringBuffer("redirect:").append(req_uri);
            buffer.append("?client_id=").append(parameter.getClient_id());
            buffer.append("&response_type=").append(parameter.getResponse_type());
            buffer.append("&redirect_uri=").append(URLEncoder.encode(parameter.getRedirect_uri(), "UTF-8"));
            buffer.append("&state=").append(parameter.getState());
            buffer.append("&error=").append(URLEncoder.encode(parameter.getError(), "UTF-8"));
            buffer.append("&username=").append(parameter.getUsername());
            return new FastView(buffer.toString()).done();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 初始化OAUTH2.0登录界面
    private ModelAndView toInitLoginView(OAuth2ClientParam parameter) {
        String client_pbk = encryptService.md5(parameter.getClient_id() + System.currentTimeMillis());
        parameter.setClient_pbk(client_pbk);
        SessionUtils.setAttribute(CLIENT_PBK, client_pbk);
        return new FastView(oAuth2TokenService.getOAuth2LoginView())
                .add("client", parameter)
                .done();
    }

    // 输出失败异常JSON数据对象
    private Object responseFailEntity(String errorCode, String errorMsg, int status) throws BizErrorEx {
        throw new BizErrorEx(errorCode, errorMsg, HttpStatus.valueOf(status));
    }

    private String getIpAddr(HttpServletRequest request) {
        return HttpUtils.getIpAddr(request);
    }

    private OAuth2ClientParam buildAuthorizeParam(HttpServletRequest request) {
        String redirect_uri = request.getParameter(OAuth.OAUTH_REDIRECT_URI);
        String state = request.getParameter(OAuth.OAUTH_STATE);
        String client_id = request.getParameter(OAuth.OAUTH_CLIENT_ID);
        String response_type = request.getParameter(OAuth.OAUTH_RESPONSE_TYPE);
        String username = StringUtils.isEmpty(request.getParameter(USERNAME)) ? "" : request.getParameter(USERNAME);
        String password = StringUtils.isEmpty(request.getParameter(PASSWORD)) ? "" : request.getParameter(PASSWORD);
        String error = StringUtils.isEmpty(request.getParameter(ERROR)) ? "" : request.getParameter(ERROR);
        String client_pbk = request.getParameter(CLIENT_PBK);
        OAuth2ClientParam param = new OAuth2ClientParam();
        param.setRedirect_uri(redirect_uri);
        param.setState(state);
        param.setClient_id(client_id);
        param.setResponse_type(response_type);
        param.setUsername(username);
        param.setPassword(password);
        param.setError(error);
        param.setClient_pbk(client_pbk);
        return param;
    }

}
