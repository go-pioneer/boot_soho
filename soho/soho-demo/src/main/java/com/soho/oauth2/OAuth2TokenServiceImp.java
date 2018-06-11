package com.soho.oauth2;

import com.soho.demo.dao.OauthClientDAO;
import com.soho.demo.dao.OauthClientTokenDAO;
import com.soho.demo.dao.OauthUserDAO;
import com.soho.demo.domain.OauthClient;
import com.soho.demo.domain.OauthClientToken;
import com.soho.demo.domain.OauthUser;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2ErrorCode;
import com.soho.oauth2.model.OAuth2Token;
import com.soho.oauth2.service.imp.AbstractOAuth2TokenService;
import com.soho.spring.model.OAuthData;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.security.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by shadow on 2018/1/14.
 */
@Service
public class OAuth2TokenServiceImp extends AbstractOAuth2TokenService {

    @Autowired
    private OAuthData oAuthData;
    @Autowired
    private EncryptService encryptService;

    @Autowired
    private OauthUserDAO oauthUserDAO;
    @Autowired
    private OauthClientDAO oauthClientDAO;
    @Autowired
    private OauthClientTokenDAO oauthClientTokenDAO;

    @Override
    public void addClientTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = new OauthClientToken();
            clientToken.setUid(oAuth2Token.getUid());
            clientToken.setClient_id(oAuth2Token.getClient_id());
            clientToken.setCode(oAuth2Token.getCode());
            clientToken.setAccess_token(oAuth2Token.getAccess_token());
            clientToken.setRefresh_token(oAuth2Token.getRefresh_token());
            clientToken.setRefresh_time(oAuth2Token.getAccess_time());
            clientToken.setAccess_time(oAuth2Token.getAccess_time());
            clientToken.setCode_state(oAuth2Token.getCode_state());
            clientToken.setToken_state(oAuth2Token.getToken_state());
            clientToken.setLogout_time(oAuth2Token.getLogout_time());
            clientToken.setCode_expire(oAuth2Token.getAccess_time() + oAuthData.getCodeExpire());
            clientToken.setToken_expire(oAuth2Token.getAccess_time() + oAuthData.getTokenExpire());
            clientToken.setCtime(System.currentTimeMillis());
            clientToken.setRedirect_uri(oAuth2Token.getRedirect_uri());
            oauthClientTokenDAO.insert(clientToken);
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_BUILD_FAIL, "令牌生成失败,请重新尝试");
        }
    }

    @Override
    public OAuth2Token getOAuth2TokenBySelf(String access_token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("access_token", access_token));
            return transform(clientToken);
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_QUERY_FAIL, "获取令牌失败,请重新尝试");
        }
    }

    @Override
    public OAuth2Token getAccessTokenBySelf(String client_id, String code) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("client_id", client_id).eq("code", code));
            return transform(clientToken);
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_QUERY_FAIL, "获取令牌失败,请重新尝试");
        }
    }

    @Override
    public OAuth2Token delAuthorizationCode(String client_id, String access_token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("client_id", client_id).eq("access_token", access_token));
            clientToken.setCode_state(2);
            clientToken.setUtime(System.currentTimeMillis());
            oauthClientTokenDAO.update(clientToken);
            return transform(clientToken);
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public OAuth2Client getOAuth2ClientBySelf(String client_id) throws BizErrorEx {
        try {
            OauthClient client = oauthClientDAO.findOneByCnd(new SQLCnd().eq("client_id", client_id).eq("state", 1));
            if (client != null) {
                OAuth2Client oAuth2Client = new OAuth2Client();
                oAuth2Client.setClient_id(client.getClient_id());
                oAuth2Client.setClient_secret(client.getClient_secret());
                oAuth2Client.setDomain_uri(client.getDomain_uri());
                oAuth2Client.setUsestate(client.getUsestate());
                oAuth2Client.setBindIp(client.getBindIp());
                return oAuth2Client;
            }
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> getOAuthUser(String uid) throws BizErrorEx {
        try {
            OauthUser user = oauthUserDAO.findOneByCnd(new SQLCnd().eq("uid", uid));
            if (user != null) {
                return new FastMap()
                        .add("uid", user.getUid())
                        .add("username", user.getUsername())
                        .add("nickname", StringUtils.isEmpty(user.getNickname()) ? "暂无" : user.getNickname())
                        .add("company", StringUtils.isEmpty(user.getCompany()) ? "暂无" : user.getCompany())
                        .done();
            }
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        throw new BizErrorEx(OAuth2ErrorCode.OAUTH_USER_NULL, "用户数据不存在");
    }

    @Override
    public OAuth2Token logoutTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("client_id", oAuth2Token.getClient_id()).eq("access_token", oAuth2Token.getAccess_token()));
            clientToken.setToken_state(2);
            clientToken.setLogout_time(System.currentTimeMillis());
            clientToken.setUtime(System.currentTimeMillis());
            oauthClientTokenDAO.update(clientToken);
            return oAuth2Token;
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_LOGOUT_FAIL, "令牌注销失败,请重新尝试");
        }
    }

    @Override
    public OAuth2Token refreshTokenBySelf(OAuth2Token oAuth2Token) throws BizErrorEx {
        try {
            OauthClientToken clientToken = oauthClientTokenDAO.findOneByCnd(new SQLCnd().eq("client_id", oAuth2Token.getClient_id()).eq("access_token", oAuth2Token.getAccess_token()).eq("refresh_token", oAuth2Token.getRefresh_token()));
            if (clientToken != null) {
                long current_time = System.currentTimeMillis();
                clientToken.setToken_state(1);
                clientToken.setRefresh_time(current_time);
                clientToken.setToken_expire(current_time + oAuthData.getTokenExpire());
                clientToken.setUtime(current_time);
                oauthClientTokenDAO.update(clientToken);
                return transform(clientToken);
            }
        } catch (MybatisDAOEx ex) {
            ex.printStackTrace();
        }
        throw new BizErrorEx(OAuth2ErrorCode.OAUTH_TOKEN_LOGOUT_FAIL, "令牌续期失败,请重新尝试");
    }

    @Override
    public OAuth2Token loginByUsername(Map<String, Object> map) throws BizErrorEx {
        try {
            String username = map.get("username").toString();
            String password = map.get("password").toString();
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
                throw new BizErrorEx(OAuth2ErrorCode.OAUTH_LOGIN_NULL, "登录失败,帐号/密码不能为空");
            }
            OauthUser user = oauthUserDAO.findOneByCnd(new SQLCnd().eq("password", encryptService.md5(password)).eq("username", username));
            if (user != null) {
                OAuth2Token oAuth2Token = new OAuth2Token();
                oAuth2Token.setUid(user.getUid());
                return oAuth2Token;
            }
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_LOGIN_ERROR, "登录失败,帐号/密码错误");
        } catch (MybatisDAOEx ex) {
            throw new BizErrorEx(OAuth2ErrorCode.OAUTH_USER_FIND_ERROR, "查询用户数据失败,请重新操作");
        }
    }

    private OAuth2Token transform(OauthClientToken clientToken) {
        if (clientToken != null) {
            OAuth2Token oAuth2Token = new OAuth2Token();
            oAuth2Token.setUid(clientToken.getUid());
            oAuth2Token.setClient_id(clientToken.getClient_id());
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
            oAuth2Token.setRedirect_uri(clientToken.getRedirect_uri());
            return oAuth2Token;
        }
        return null;
    }

}
