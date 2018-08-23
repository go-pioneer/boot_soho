package com.soho.oauth2.service.imp;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2Config;
import com.soho.oauth2.model.OAuth2ErrorCode;
import com.soho.oauth2.model.OAuth2Token;
import com.soho.oauth2.service.OAuth2TokenService;
import com.soho.spring.model.DeftConfig;
import com.soho.spring.security.EncryptService;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by shadow on 2017/5/1.
 */
public abstract class AbstractOAuth2TokenService implements OAuth2TokenService {

    @Autowired(required = false)
    private EncryptService encryptService;
    @Autowired
    private OAuth2Config oAuth2Config;
    @Autowired
    private DeftConfig deftConfig;

    // 扩展接口,保存授权数据到系统
    public abstract void addClientTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx;

    // 扩展接口,读取系统授权数据
    public abstract OAuth2Token getOAuth2TokenBySelf(String access_token) throws BizErrorEx;

    // 扩展接口,通过客户端ID和授权码获取授权令牌
    public abstract OAuth2Token getAccessTokenBySelf(String client_id, String code) throws BizErrorEx;

    // 扩展接口,设置授权码失效
    public abstract OAuth2Token delAuthorizationCode(String client_id, String access_token) throws BizErrorEx;

    // 扩展接口,通过客户端ID获取客户端配置参数
    public abstract OAuth2Client getOAuth2ClientBySelf(String client_id) throws BizErrorEx;

    // 扩展接口,通过UID获取系统用户数据
    public abstract Map<String, Object> getOAuthUser(String uid) throws BizErrorEx;

    // 扩展接口,注销授权令牌
    public abstract OAuth2Token logoutTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx;

    // 扩展接口,续期授权令牌
    public abstract OAuth2Token refreshTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx;

    // 扩展接口,通过帐号信息登录认证
    public abstract OAuth2Token loginByUsername(Map<String, Object> map) throws BizErrorEx;

    // 扩展接口,OAUTH2.0系统域名
    public String getOAuth2DomainUri() {
        return oAuth2Config.getDomain();
    }

    protected String getEncryptyKey() {
        return encryptService.aes_d(deftConfig.getProjectKey());
    }

    @Override
    public OAuth2Token addClientToken(OAuth2Token oAuth2Token) throws BizErrorEx {
        oAuth2Token.setAccess_time(System.currentTimeMillis()); // 授权时间
        oAuth2Token.setCode_expire(oAuth2Token.getAccess_time() + oAuth2Config.getCodeExpire());
        oAuth2Token.setToken_expire(oAuth2Token.getAccess_time() + oAuth2Config.getTokenExpire());
        oAuth2Token.setRefresh_time(oAuth2Token.getAccess_time()); // 令牌重新授权刷新时间
        oAuth2Token.setCode_state(1); // 授权码状态 1.正常 2.失效
        oAuth2Token.setToken_state(1); // 授权令牌状态 1.正常 2.失效
        oAuth2Token.setLogout_time(0l); // 授权令牌注销时间
        addClientTokenBySelf(oAuth2Token); // 保存令牌接口
        return oAuth2Token;
    }

    @Override
    public OAuth2Token validAccessPbk(String access_token, String access_pbk) throws BizErrorEx {
        return validAccessPbk(access_token, access_pbk, true);
    }

    @Override
    public OAuth2Token validAccessPbk(String access_token, String access_pbk, boolean invalid_token) throws BizErrorEx {
        if (StringUtils.isEmpty(access_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_NULL, "客户端请求参数【access_token】不能为空");
        }
        if (StringUtils.isEmpty(access_pbk)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_PBK_NULL, "客户端请求参数【access_pbk】不能为空");
        }
        String build_pbk = buildAccessPbk(access_token);
        if (!access_pbk.equals(build_pbk)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_PBK_INVALID, "客户端请求参数【access_pbk】校验失败");
        }
        return getOAuth2Token(access_token, invalid_token);
    }

    @Override
    public OAuth2Token getOAuth2Token(String access_token) throws BizErrorEx {
        return getOAuth2Token(access_token, true);
    }

    @Override
    public OAuth2Token getOAuth2Token(String access_token, boolean invalid_token) throws BizErrorEx {
        if (StringUtils.isEmpty(access_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_NULL, "客户端请求参数【access_token】不能为空");
        }
        OAuth2Token oAuth2Token = getOAuth2TokenBySelf(access_token);
        if (oAuth2Token == null) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_ILLEGAL, "客户端请求参数【access_token】不存在");
        }
        if (invalid_token) {
            if (oAuth2Token.getToken_state() != 1) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_INVALID, "客户端请求参数【access_token】已失效");
            }
            if (oAuth2Token.getToken_expire() < System.currentTimeMillis()) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_EXPIRED, "客户端请求参数【access_token】已过期");
            }
        }
        return oAuth2Token;
    }

    @Override
    public String buildAccessPbk(String access_token) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("&access_token=").append(access_token).append("&key=").append(getEncryptyKey());
        return encryptService.md5(buffer.toString());
    }

    @Override
    public OAuth2Client validOAuth2Client(String client_id, String request_ip) throws BizErrorEx {
        if (StringUtils.isEmpty(client_id)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ID_NULL, "客户端请求参数【client_id】不能为空");
        }
        OAuth2Client oAuth2Client = getOAuth2ClientBySelf(client_id);
        if (oAuth2Client == null) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】不匹配");
        }
        if (StringUtils.isEmpty(oAuth2Client.getDomain_uri())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】尚未绑定域名");
        }
        if (StringUtils.isEmpty(oAuth2Client.getBindIp())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】尚未绑定服务器IP");
        }
        if (!StringUtils.isEmpty(request_ip) && !request_ip.equals(oAuth2Client.getBindIp())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】服务器IP校验失败");
        }
        if (StringUtils.isEmpty(oAuth2Client.getUsestate())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】尚未开通");
        }
        if (oAuth2Client.getUsestate() == 2) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】已被冻结");
        }
        if (oAuth2Client.getUsestate() == 3) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_id】已被禁用");
        }
        return oAuth2Client;
    }

    @Override
    public OAuth2Token getAccessTokenByCode(String client_id, String code, String redirect_uri) throws BizErrorEx {
        if (StringUtils.isEmpty(client_id)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ID_NULL, "客户端请求参数【client_id】不能为空");
        }
        if (StringUtils.isEmpty(code)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_NULL, "客户端请求参数【code】不能为空");
        }
        OAuth2Token oAuth2Token = getAccessTokenBySelf(client_id, code);
        if (oAuth2Token == null) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_ILLEGAL, "客户端请求参数【code】不存在");
        }
        if (StringUtils.isEmpty(oAuth2Token.getRedirect_uri()) || !oAuth2Token.getRedirect_uri().equals(redirect_uri)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_REDIRCTURI_INVALID, "客户端请求参数【redirect_uri】重定向地址错误");
        }
        if (oAuth2Token.getCode_state() != 1) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_INVALID, "客户端请求参数【code】已失效");
        }
        if (oAuth2Token.getCode_expire() < System.currentTimeMillis()) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_EXPIRED, "客户端请求参数【code】已过期");
        }
        return delAuthorizationCode(oAuth2Token.getClient_id(), oAuth2Token.getAccess_token());
    }

    @Override
    public void validClientSecret(OAuth2Client oAuth2Client, String client_secret) throws BizErrorEx {
        if (StringUtils.isEmpty(client_secret)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_secret】不能为空");
        }
        if (!encryptService.aes_e(client_secret).equals(oAuth2Client.getClient_secret())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【client_secret】校验失败");
        }
    }

    @Override
    public void validRedirectUri(OAuth2Client oAuth2Client, String redirect_uri) throws BizErrorEx {
        if (StringUtils.isEmpty(redirect_uri) || redirect_uri.length() > 255) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【redirect_uri】不能为空");
        }
        if (!redirect_uri.startsWith(oAuth2Client.getDomain_uri())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【redirect_uri】校验失败");
        }
    }

    @Override
    public String validState(String state) throws BizErrorEx {
        if (StringUtils.isEmpty(state)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【state】不能为空");
        }
        if (state.length() > 100) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【state】长度超出范围");
        }
        return state;
    }

    @Override
    public String validResponseType(String responseType) throws BizErrorEx {
        if (StringUtils.isEmpty(responseType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【response_type】不能为空");
        }
        // responseType目前仅支持CODE，另外还有TOKEN
        if (!ResponseType.CODE.toString().equals(responseType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【response_type】不支持");
        }
        return responseType;
    }

    @Override
    public String validGrantType(String grantType) throws BizErrorEx {
        if (StringUtils.isEmpty(grantType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【grant_type】不能为空");
        }
        // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
        if (!GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【grant_type】不支持");
        }
        return grantType;
    }

    @Override
    public OAuth2Token logoutToken(OAuth2Token oAuth2Token) throws BizErrorEx {
        return logoutTokenBySelf(oAuth2Token);
    }

    @Override
    public OAuth2Token refreshToken(String access_token, String refresh_token) throws BizErrorEx {
        if (StringUtils.isEmpty(refresh_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【refresh_token】不能为空");
        }
        if (StringUtils.isEmpty(access_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【access_token】不能为空");
        }
        OAuth2Token oAuth2Token = getOAuth2TokenBySelf(access_token);
        if (oAuth2Token == null) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_ILLEGAL, "客户端请求参数【access_token】不存在");
        }
        if (!oAuth2Token.getRefresh_token().equals(refresh_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端请求参数【refresh_token】校验失败");
        }
        return refreshTokenBySelf(oAuth2Token);
    }

    @Override
    public String getOAuth2LoginView() {
        return oAuth2Config.getLoginView();
    }

    @Override
    public void validJaqState() throws BizErrorEx {

    }

    @Override
    public void delJaqState() {

    }

}
