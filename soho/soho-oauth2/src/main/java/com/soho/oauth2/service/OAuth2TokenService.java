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
     * @param request_ip
     */
    public OAuth2Client validOAuth2Client(String client_id, String request_ip) throws BizErrorEx;

    /**
     * 校验客户端配置参数是否正常
     *
     * @param oAuth2Client  系统客户端配置
     * @param redirect_uri  客户端重定向地址
     * @param valid_secret  是否校验客户端密钥
     * @param client_secret 客户端密钥参数
     */
    public void validClientConfig(OAuth2Client oAuth2Client, String redirect_uri, boolean valid_secret, String client_secret) throws BizErrorEx;

    /**
     * 校验code是否有效
     *
     * @param client_id
     * @param code
     * @return OAuth2Token
     */
    public OAuth2Token getAccessTokenByCode(String client_id, String code) throws BizErrorEx;

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
     * @return Map<String, Object>
     */
    public Map<String, Object> getOAuthUser(String uid) throws BizErrorEx;

    /**
     * 注销access_token
     *
     * @param oAuth2Token
     * @return OAuth2Token
     */
    public OAuth2Token logoutToken(OAuth2Token oAuth2Token) throws BizErrorEx;


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
     * @param map
     * @return OAuth2Token
     */
    public OAuth2Token loginByUsername(Map<String, Object> map) throws BizErrorEx;

    /**
     * 生成二次认证的密钥,防止暴力破解
     *
     * @param client_id
     * @param access_time
     * @param access_token
     * @return String
     */
    public String buildAccessPbk(String client_id, long access_time, String access_token);

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
