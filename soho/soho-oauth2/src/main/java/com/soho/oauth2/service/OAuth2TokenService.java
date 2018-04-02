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
     * 保存授权码和用户ID的关系
     *
     * @param clientId      授权clientId
     * @param uid           用户UID
     * @param username      用户名
     * @param code          授权码
     * @param token         授权token
     * @param refresh_token 刷新token
     */
    public OAuth2Token addClientToken(String clientId, String uid, String username, String code, String token, String refresh_token) throws BizErrorEx;

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
     * @param clientId
     */
    public OAuth2Client validClientId(String clientId) throws BizErrorEx;

    /**
     * 校验client_secret参数是否正常
     *
     * @param appKey       系统配置密钥
     * @param clientSecret 用户参数密钥
     */
    public void validClientSecret(String appKey, String clientSecret) throws BizErrorEx;

    /**
     * 校验authCode是否有效
     *
     * @param clientId
     * @param authCode
     * @return OauthClientToken
     */
    public OAuth2Token getTokenByCode(String clientId, String authCode) throws BizErrorEx;

    /**
     * authCode设置已使用
     *
     * @param access_token
     */
    public OAuth2Token delAuthCode(String access_token) throws BizErrorEx;

    /**
     * 获取clientId对应的回调地址
     *
     * @param clientId
     * @param redirect_uri
     * @return String
     */
    public String validRredirectUri(String clientId, String redirect_uri) throws BizErrorEx;

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
     * @param clientId
     * @param refresh_token
     * @param access_token
     * @param access_pbk
     * @return OAuth2Token
     * @throws BizErrorEx
     */
    public OAuth2Token refreshToken(String clientId, String refresh_token, String access_token, String access_pbk) throws BizErrorEx;

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
     * @param clientId
     * @param access_token
     * @return String
     */
    public String buildAccessPbk(String clientId, String access_token);

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
