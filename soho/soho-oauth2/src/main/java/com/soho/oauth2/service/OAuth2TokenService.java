package com.soho.oauth2.service;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.model.OAuth2Client;
import com.soho.oauth2.model.OAuth2Token;

import java.util.Map;

/**
 * Created by shadow on 2017/5/1.
 */
public interface OAuth2TokenService {

    /**
     * 保存授权对象
     *
     * @param oAuth2Token 授权对象
     */
    public OAuth2Token addClientToken(OAuth2Token oAuth2Token) throws BizErrorEx;

    /**
     * 校验client_id参数是否正常
     *
     * @param access_token
     * @param access_pbk
     */
    public OAuth2Token validAccessPbk(String access_token, String access_pbk) throws BizErrorEx;

    /**
     * 校验client_id参数是否正常
     *
     * @param client_id
     */
    public OAuth2Client validOAuth2Client(String client_id) throws BizErrorEx;

    /**
     * 校验client_secret参数是否正常
     *
     * @param client_secret1 系统配置密钥
     * @param client_secret2 用户参数密钥
     */
    public void validClientSecret(String client_secret1, String client_secret2) throws BizErrorEx;

    /**
     * 校验authCode是否有效
     *
     * @param client_id
     * @param authCode
     * @return OauthClientToken
     */
    public OAuth2Token getTokenByCode(String client_id, String authCode) throws BizErrorEx;

    /**
     * authCode设置已使用
     *
     * @param access_token
     */
    public OAuth2Token delAuthCode(String access_token) throws BizErrorEx;

    /**
     * 获取client_id对应的回调地址
     *
     * @param client_id
     * @param redirect_uri
     * @return String
     */
    public String validRredirectUri(String client_id, String redirect_uri) throws BizErrorEx;

    /**
     * 校验state参数是否为空
     *
     * @param state
     * @return
     * @throws BizErrorEx
     */
    public String validState(String state) throws BizErrorEx;

    /**
     * 校验responseType参数是否正确
     *
     * @param responseType
     * @throws BizErrorEx
     */
    public String validResponseType(String responseType) throws BizErrorEx;

    /**
     * 校验grantType参数是否正确
     *
     * @param grantType
     * @throws BizErrorEx
     */
    public String validGrantType(String grantType) throws BizErrorEx;

    /**
     * 通过access_token获取认证数据
     *
     * @param access_token
     * @return OauthClientToken
     */
    public OAuth2Token getOAuth2Token(String access_token) throws BizErrorEx;

    /**
     * 获取oauth用户信息
     *
     * @param uid
     * @return OauthUser
     */
    public Map<String, String> getOauthUser(String uid) throws BizErrorEx;

    /**
     * 注销access_token
     *
     * @param access_token
     * @return OauthClientToken
     */
    public OAuth2Token logoutToken(String access_token, String access_pbk) throws BizErrorEx;


    /**
     * 续期access_token
     *
     * @param client_id
     * @param refresh_token
     * @param access_token
     * @param access_pbk
     * @return OAuth2Token
     * @throws BizErrorEx
     */
    public OAuth2Token refreshToken(String client_id, String refresh_token, String access_token, String access_pbk) throws BizErrorEx;

    /**
     * 帐号密码登录
     *
     * @param username
     * @param password
     * @return OauthUser
     */
    public Map<String, String> loginByUsername(String username, String password) throws BizErrorEx;

    /**
     * 生成二次认证的密钥,防止暴力破解
     *
     * @param client_id
     * @param access_token
     * @return String
     */
    public String buildAccessPbk(String client_id, String access_token);

    /**
     * OAUTH2登录WebView
     *
     * @return String
     */
    public String getOAuth2LoginView();

    /**
     * OAUTH2登录域名
     *
     * @return String
     */
    public String getOAuth2DomainUri();

    /**
     * 登录认证校验状态
     */
    public void validJaqState() throws BizErrorEx;

    /**
     * 删除登录认证状态
     */
    public void delJaqState();

}
