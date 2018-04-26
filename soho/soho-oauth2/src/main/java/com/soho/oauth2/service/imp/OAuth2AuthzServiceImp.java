package com.soho.oauth2.service.imp;

import com.alibaba.fastjson.JSON;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2ErrorCode;
import com.soho.oauth2.model.OAuth2Token;
import com.soho.oauth2.service.OAuth2AuthzService;
import com.soho.oauth2.service.OAuth2TokenService;
import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.security.EncryptService;
import com.soho.spring.shiro.utils.SessionUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
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
import java.net.URISyntaxException;
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

    // http://localhost:8011/oauth2.0/v1.0/authorize?client_id=c522f0c158d4c9d5be2f1032c38a8148&response_type=code&state=1&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Foauth2.0%2Fcallback

    @Override
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        String redirect_uri = null;
        try {
            // 构建OAuth 授权请求
            OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            // 获取客户端重定向地址
            redirect_uri = oAuthAuthzRequest.getRedirectURI();
            // 检查传入的客户端参数
            try {
                oAuth2TokenService.validState(oAuthAuthzRequest.getState());
                oAuth2TokenService.validResponseType(oAuthAuthzRequest.getParam(OAuth.OAUTH_RESPONSE_TYPE));
                oAuth2TokenService.validOAuth2Client(oAuthAuthzRequest.getClientId());
                oAuth2TokenService.validRredirectUri(oAuthAuthzRequest.getClientId(), redirect_uri);
            } catch (BizErrorEx e) {
                return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
            }
            OAuth2Token oAuth2Token = null;
            String username = StringUtils.isEmpty(oAuthAuthzRequest.getParam("username")) ? "" : oAuthAuthzRequest.getParam("username");
            String password = StringUtils.isEmpty(oAuthAuthzRequest.getParam("password")) ? "" : oAuthAuthzRequest.getParam("password");
            String error = StringUtils.isEmpty(oAuthAuthzRequest.getParam("error")) ? "" : oAuthAuthzRequest.getParam("error");
            // 校验PBK签名
            Object s_pbk = SessionUtils.getAttribute(CLIENT_PBK);
            String pbk = oAuthAuthzRequest.getParam(CLIENT_PBK);
            if (StringUtils.isEmpty(s_pbk) || !s_pbk.equals(pbk)) {
                return toInitLoginView(oAuthAuthzRequest, redirect_uri, username, error);
            }
            try {
                oAuth2TokenService.validJaqState();
                Map<String, Object> loginInfo = new FastMap().add("username", username).add("password", password).done();
                oAuth2Token = oAuth2TokenService.loginByUsername(loginInfo);
                OAuthIssuer oAuthIssuer = new OAuthIssuerImpl(new MD5Generator());
                oAuth2Token.setClient_id(oAuthAuthzRequest.getClientId());
                oAuth2Token.setCode(oAuthIssuer.authorizationCode());
                oAuth2Token.setAccess_token(oAuthIssuer.accessToken());
                oAuth2Token.setRefresh_token(oAuthIssuer.accessToken());
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
        } catch (OAuthProblemException e) { //返回错误消息（如?error=）
            if (StringUtils.isEmpty(redirect_uri)) {
                return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
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
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求失败", HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
        } catch (Exception e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
        }
    }

    @Override
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        try {
            OAuth2Token oAuth2Token = null;
            OAuth2Client oAuth2Client = null;
            try {
                // 构建OAuth请求
                OAuthTokenRequest oAuthTokenRequest = new OAuthTokenRequest(request);
                // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
                oAuth2TokenService.validGrantType(oAuthTokenRequest.getParam(OAuth.OAUTH_GRANT_TYPE));
                // 检查提交的客户端id是否正确
                oAuth2Client = oAuth2TokenService.validOAuth2Client(oAuthTokenRequest.getClientId());
                // 检查客户端安全KEY是否正确
                oAuth2TokenService.validClientSecret(oAuth2Client.getClient_secret(), oAuthTokenRequest.getClientSecret());
                // 检查客户端重定向
                oAuth2TokenService.validRredirectUri(oAuth2Client.getClient_id(), oAuthTokenRequest.getRedirectURI());
                // 获取授权码并进行认证
                String code = oAuthTokenRequest.getParam(OAuth.OAUTH_CODE);
                oAuth2Token = oAuth2TokenService.getAccessTokenByCode(oAuth2Client.getClient_id(), code);
            } catch (BizErrorEx e) {
                return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT);
            } catch (OAuthSystemException e) {
                return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
            }
            // 生成OAuth响应
            Map<String, Object> map = new FastMap()
                    .add("access_token", oAuth2Token.getAccess_token())
                    .add("access_pbk", oAuth2TokenService.buildAccessPbk(oAuth2Client.getClient_id(), oAuth2Token.getAccess_token()))
                    .add("access_time", oAuth2Token.getAccess_time())
                    .add("refresh_token", oAuth2Token.getRefresh_token())
                    .add("refresh_time", oAuth2Token.getRefresh_time())
                    .add("token_expire", oAuth2Token.getToken_expire())
                    .done();
            return responseSuccessEntity(map);
        } catch (OAuthProblemException e) {
            return responseFailEntity(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
        }
    }

    @Override
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Access Token
            String accessToken = oAuthAccessResourceRequest.getAccessToken();
            // 获取令牌公钥
            String accessPbk = request.getParameter("access_pbk");
            try {
                // 校验客户端公钥
                OAuth2Token oAuth2Token = oAuth2TokenService.validAccessPbk(accessToken, accessPbk);
                // 读取OAuth用户信息
                Map<String, Object> user = oAuth2TokenService.getOAuthUser(oAuth2Token.getUid());
                // 生成OAuth响应
                Map<String, Object> resp = toBuildJsonMapResponse(RetCode.OK_STATUS, "", HttpServletResponse.SC_OK, "");
                return new FastMap().addAll(resp).addAll(user).done();
            } catch (BizErrorEx ex) {
                return toBuildJsonMapResponse(ex.getErrorCode(), ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_GRANT);
            }
        } catch (OAuthProblemException e) {
            return toBuildJsonMapResponse(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
        }
    }

    @Override
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Client_Id
            String clientId = request.getParameter("client_id");
            // 获取Access_Token
            String accessToken = oAuthAccessResourceRequest.getAccessToken();
            // 获取Refresh_Token
            String refreshToken = request.getParameter("refresh_token");
            // 获取令牌公钥
            String accessPbk = request.getParameter("access_pbk");
            // 延期Access_Token授权时间
            try {
                if (StringUtils.isEmpty(clientId) || StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessPbk)) {
                    return toBuildJsonMapResponse(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "Required parameter missing", HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
                }
                OAuth2Token oAuth2Token = new OAuth2Token();
                oAuth2Token.setClient_id(clientId);
                oAuth2Token.setAccess_token(accessToken);
                oAuth2Token.setRefresh_token(refreshToken);
                oAuth2TokenService.refreshToken(clientId, refreshToken, accessToken, accessPbk);
            } catch (BizErrorEx ex) {
                return toBuildJsonMapResponse(ex.getErrorCode(), ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
            }
            // 根据OAuthResponse生成ResponseEntity
            return toBuildJsonMapResponse(RetCode.OK_STATUS, "", HttpServletResponse.SC_OK, "");
        } catch (OAuthProblemException e) {
            // 构建错误响应
            return toBuildJsonMapResponse(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
        }
    }

    @Override
    public Object logout_token(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException {
        try {
            // 构建OAuth资源请求
            OAuthAccessResourceRequest oAuthAccessResourceRequest = new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            // 获取Access Token
            String accessToken = oAuthAccessResourceRequest.getAccessToken();
            // 获取令牌公钥
            String accessPbk = request.getParameter("access_pbk");
            // 注销Access Token
            try {
                oAuth2TokenService.logoutToken(accessToken, accessPbk);
            } catch (BizErrorEx ex) {
                return toBuildJsonMapResponse(ex.getErrorCode(), ex.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
            }
            // 根据OAuthResponse生成ResponseEntity
            return toBuildJsonMapResponse(RetCode.OK_STATUS, "", HttpServletResponse.SC_OK, "");
        } catch (OAuthProblemException e) {
            // 构建错误响应
            return toBuildJsonMapResponse(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST, OAuthError.TokenResponse.INVALID_REQUEST);
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

    // 输出业务成功JSON数据对象
    private Object responseSuccessEntity(Map<String, Object> entity) throws BizErrorEx {
        Map<String, Object> map = new FastMap()
                .add("status", HttpServletResponse.SC_OK)
                .add("error", "")
                .addAll(entity)
                .done();
        return map;
    }

    // 输出失败异常JSON数据对象
    private Object responseFailEntity(String code, String errorMsg, int status, String error) throws BizErrorEx {
        Map<String, Object> map = new FastMap()
                .add("status", status)
                .add("error", error)
                .done();
        throw new BizErrorEx(code, errorMsg, map);
    }

    // 输出浏览器数据对象(UTF-8编码)
    private ResponseEntity toBuildUTF8WebResponse(BizErrorEx ex, int errorResponse, String error) throws OAuthSystemException {
        Map<String, Object> map = toBuildJsonMapResponse(ex.getErrorCode(), ex.getMessage(), errorResponse, error);
        RetData ret = new RetData(RetCode.OK_STATUS, RetCode.OK_MESSAGE, map);
        return new ResponseEntity(JSON.toJSONString(ret), buildHttpUtf8Header(), HttpStatus.valueOf(errorResponse));
    }

    // 输出异常信息对象(包含http状态,业务状态)
    private Map<String, Object> toBuildJsonMapResponse(String errorCode, String errorMsg, int errorResponse, String error) {
        return new FastMap()
                .add("biz_code", errorCode)
                .add("biz_error", errorMsg)
                .add("http_code", errorResponse)
                .add("http_error", error)
                .done();
    }

    private HttpHeaders buildHttpUtf8Header() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "text/html;charset=UTF-8");
        return headers;
    }

}
