package com.soho.oauth2.service.imp;

import com.goldsaints.mofaxin.ethfans.domain.OauthClient;
import com.goldsaints.mofaxin.ethfans.domain.OauthClientToken;
import com.goldsaints.mofaxin.ethfans.domain.OauthUser;
import com.goldsaints.mofaxin.ethfans.domain.YtfUserInfo;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2ErrorCode;
import com.soho.oauth2.model.OAuth2Token;
import com.soho.oauth2.service.OAuth2TokenService;
import com.soho.spring.security.EncryptService;
import com.soho.utils.MD5Util;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shadow on 2017/5/1.
 */
public abstract class AbstractOAuth2TokenService implements OAuth2TokenService {

    private static final String PBK_KEY = "p/ZT!dl%8";

    @Autowired(required = false)
    private EncryptService encryptService;

    // 扩展接口,保存授权数据到系统
    public abstract void addClientTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx;

    // 扩展接口,读取系统授权数据
    public abstract OAuth2Token getOAuth2TokenBySelf(String access_token) throws BizErrorEx;

    @Override
    public OAuth2Token addClientToken(OAuth2Token oAuth2Token) throws BizErrorEx {
        oAuth2Token.setAccess_time(System.currentTimeMillis()); // 授权时间
        oAuth2Token.setRefresh_time(oAuth2Token.getAccess_time()); // 令牌重新授权刷新时间
        oAuth2Token.setCode_state(1); // 授权码状态 1.正常 2.失效
        oAuth2Token.setToken_state(1); // 授权令牌状态 1.正常 2.失效
        oAuth2Token.setLogout_time(0l); // 授权令牌注销时间
        // oAuth2Token.setCode_expire(0l); // 授权码过期时间,用户自定义
        // oAuth2Token.setToken_expire(0l); // 授权令牌过期时间,用户自定义
        addClientTokenBySelf(oAuth2Token); // 保存令牌接口
        return oAuth2Token;
    }

    @Override
    public OAuth2Token validAccessPbk(String access_token, String access_pbk) throws BizErrorEx {
        if (StringUtils.isEmpty(access_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_NULL, "令牌不能为空");
        }
        if (StringUtils.isEmpty(access_pbk)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_PBK_NULL, "令牌公钥为空");
        }
        OAuth2Token oAuth2Token = getOAuth2Token(access_token);
        String client_id = oAuth2Token.getClient_id();
        String build_pbk = buildAccessPbk(client_id, access_token);
        if (!access_pbk.equals(build_pbk)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_NULL, "令牌公钥无效");
        }
        return oAuth2Token;
    }

    @Override
    public OAuth2Token getOAuth2Token(String access_token) throws BizErrorEx {
        if (StringUtils.isEmpty(access_token)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_NULL, "令牌不能为空");
        }
        OAuth2Token oAuth2Token = getOAuth2TokenBySelf(access_token);
        if (oAuth2Token == null) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_ILLEGAL, "令牌不存在");
        }
        if (oAuth2Token.getToken_state() != 1) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_INVALID, "令牌已失效");
        }
        if (oAuth2Token.getToken_expire() < System.currentTimeMillis()) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_EXPIRED, "令牌已过期");
        }
        return oAuth2Token;
    }

    @Override
    public String buildAccessPbk(String client_id, String access_token) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("client_id=").append(client_id).append("&access_token=").append(access_token).append("&key=").append(PBK_KEY);
        return encryptService.md5(buffer.toString());
    }

    @Override
    public abstract OAuth2Client validOAuth2Client(String client_id) throws BizErrorEx;

    @Override
    public void validClientSecret(String client_secret1, String client_secret2) throws BizErrorEx {
        if (StringUtils.isEmpty(client_secret1) || !client_secret1.equals(client_secret2)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端密钥校验失败");
        }
    }

    @Override
    public OAuth2Token getTokenByCode(String appId, String authCode) throws BizErrorEx {
        if (StringUtils.isEmpty(authCode)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_NULL, "授权码不能为空");
        }
        try {
            OauthClientToken token = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("appId", appId).eq("code", authCode));
            if (token == null) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_ILLEGAL, "授权码不存在");
            }
            if (token.getCode_state() != 1) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_INVALID, "授权码已失效");
            }
            if (token.getCode_expire() < System.currentTimeMillis()) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CODE_EXPIRED, "授权码已过期");
            }
            return delAuthCode(token.getAccess_token());
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "系统繁忙,请重新尝试");
        }
    }

    @Override
    public OAuth2Token delAuthCode(String access_token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("access_token", access_token));
            clientToken.setCode_state(2);
            clientToken.setUtime(System.currentTimeMillis());
            oauthClientTokenDAO.update(clientToken);
            return toOAuth2Token(clientToken);
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "系统繁忙,请重新尝试");
    }

    @Override
    public String validRredirectUri(String clientId, String redirect_uri) throws BizErrorEx {
        OauthClient client = getOauthClient(clientId);
        if (StringUtils.isEmpty(redirect_uri)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端回调地址参数为空");
        }
        if (StringUtils.isEmpty(client.getCallurl())) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端尚未绑定第三方域名");
        }
        if (StringUtils.isEmpty(redirect_uri) || !redirect_uri.startsWith(client.getCallurl())) { // 客户端回调地址为空或与客户端主域名不匹配
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端回调地址与第三方提供域名不匹配");
        }
        return client.getCallurl();
    }

    @Override
    public String validState(String state) throws BizErrorEx {
        if (StringUtils.isEmpty(state)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "客户端state参数为空");
        }
        return state;
    }

    @Override
    public String validResponseType(String responseType) throws BizErrorEx {
        // responseType目前仅支持CODE，另外还有TOKEN
        if (!ResponseType.CODE.toString().equals(responseType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "ResponseType参数不支持");
        }
        return responseType;
    }

    @Override
    public String validGrantType(String grantType) throws BizErrorEx {
        // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
        if (!GrantType.AUTHORIZATION_CODE.toString().equals(grantType)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "GrantType参数不支持");
        }
        return grantType;
    }

    @Override
    public Map<String, String> getOauthUser(String uid) throws BizErrorEx {
        try {
            OauthUser user = oauthUserDAO.findOneByCnd(new SQLCnd().eq("uid", uid).eq("state", 1));
            if (user != null) {
                YtfUserInfo userInfo = ytfUserInfoDAO.findOneByCnd(new SQLCnd().eq("userId", user.getId()).eq("state", 1));
                if (userInfo != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", user.getUid());
                    map.put("nickname", StringUtils.isEmpty(user.getNickname()) ? "null" : user.getNickname());
                    map.put("headimg", StringUtils.isEmpty(user.getHeadimg()) ? "http://oth3x62gr.bkt.clouddn.com/head.png" : user.getHeadimg());
                    map.put("sex", StringUtils.isEmpty(user.getSex()) ? "1" : user.getSex().toString());
                    map.put("age", StringUtils.isEmpty(user.getAge()) ? "0" : user.getAge().toString());
                    map.put("point", userInfo.getPoint());
                    map.put("asc_jt", userInfo.getAsc_jt());
                    map.put("asc_dt", userInfo.getAsc_dt());
                    map.put("asc_addr", user.getAsc_addr());
                    return map;
                }
            }
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "没有用户数据");
    }

    @Override
    public OAuth2Token logoutToken(String access_token, String access_pbk) throws BizErrorEx {
        try {
            OAuth2Token oAuth2Token = validAccessPbk(access_token, access_pbk);
            OauthClientToken token = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("access_token", oAuth2Token.getAccess_token()));
            token.setToken_state(2);
            token.setLogout_time(System.currentTimeMillis());
            token.setUtime(token.getLogout_time());
            oauthClientTokenDAO.update(token);
            return oAuth2Token;
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_CLIENT_ERROR, "系统繁忙,请重新尝试");
        }
    }

    @Override
    public Map<String, String> loginByUsername(String username, String password) throws BizErrorEx {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_LOGIN_NULL, "帐号/密码不能为空");
        }
        try {
            OauthUser user = oauthUserDAO.findOneByCnd(new SQLCnd().eq("state", 1).eq("password", MD5Util.MD5(password)).or(new SQLCnd().eq("mobile", username), new SQLCnd().eq("username", username)));
            if (user != null) {
                Map<String, String> map = new HashMap<>();
                map.put("uid", user.getUid());
                map.put("username", username);
                return map;
            }
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        throw new BizErrorEx(OAuth2ErrorCode.OAUTH_LOGIN_ERROR, "帐号/密码错误");
    }

    @Override
    public String getOAuth2LoginView() {
        return "oauth2/login";
    }

    private OAuth2Token toOAuth2Token(OauthClientToken clientToken) {
        OAuth2Token oAuth2Token = new OAuth2Token();
        oAuth2Token.setUid(clientToken.getUid());
        oAuth2Token.setUsername(clientToken.getUsername());
        oAuth2Token.setClient_id(clientToken.getAppId());
        oAuth2Token.setCode(clientToken.getCode());
        oAuth2Token.setAccess_token(clientToken.getAccess_token());
        oAuth2Token.setRefresh_token(clientToken.getRefresh_token());
        oAuth2Token.setAccess_time(clientToken.getAccess_time());
        oAuth2Token.setRefresh_time(clientToken.getRefresh_time());
        oAuth2Token.setCode_expire(clientToken.getCode_expire());
        oAuth2Token.setToken_expire(clientToken.getToken_expire());
        oAuth2Token.setToken_state(clientToken.getToken_state());
        oAuth2Token.setCode_state(clientToken.getCode_state());
        oAuth2Token.setLogout_time(clientToken.getLogout_time());
        return oAuth2Token;
    }

}
