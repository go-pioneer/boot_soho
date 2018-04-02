package com.soho.oauth2.model;

/**
 * Created by shadow on 2018/1/14.
 */
public class OAuth2ErrorCode {

    public static String OAUTH_CLIENT_ERROR = "000110"; // 第三方平台接口异常
    public static String OAUTH_LOGIN_ERROR = "000111"; // 帐号/密码错误
    public static String OAUTH_LOGIN_NULL = "000112"; // 帐号/密码为空
    public static String OAUTH_TOKEN_NULL = "000810"; // TOKEN不能为空
    public static String OAUTH_TOKEN_ILLEGAL = "000811"; // TOKEN不存在
    public static String OAUTH_TOKEN_INVALID = "000812"; // TOKEN无效
    public static String OAUTH_TOKEN_EXPIRED = "000813"; // TOKEN过期
    public static String OAUTH_CODE_NULL = "000910"; // CODE不能为空
    public static String OAUTH_CODE_ILLEGAL = "000911"; // CODE不存在
    public static String OAUTH_CODE_INVALID = "000912"; // CODE无效
    public static String OAUTH_CODE_EXPIRED = "000913"; // CODE过期

}
