package com.soho.oauth2.model;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
public class OAuth2Config {

    @Value("${oauth2.domain}")
    private String domain;
    @Value("${oauth2.codeExpire}")
    private Long codeExpire;
    @Value("${oauth2.tokenExpire}")
    private Long tokenExpire;
    @Value("${oauth2.loginView}")
    private String loginView;

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

}
