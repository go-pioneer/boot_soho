package com.soho.spring.model;


/**
 * @author shadow
 */
public class OAuthData {

    // 基本参数
    private String domain;
    private Long codeExpire;
    private Long tokenExpire;
    private String loginView;
    private String encryptyKey;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getCodeExpire() {
        return codeExpire;
    }

    public void setCodeExpire(Long codeExpire) {
        this.codeExpire = codeExpire;
    }

    public Long getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Long tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getLoginView() {
        return loginView;
    }

    public void setLoginView(String loginView) {
        this.loginView = loginView;
    }

    public String getEncryptyKey() {
        return encryptyKey;
    }

    public void setEncryptyKey(String encryptyKey) {
        this.encryptyKey = encryptyKey;
    }

}
