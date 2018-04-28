package com.soho.spring.configuration;

import com.soho.spring.model.OAuthData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
@Configuration
public class OAuthDataConfiguration {

    @Value("${oauth.domain}")
    private String domain;
    @Value("${oauth.codeExpire}")
    private Long codeExpire;
    @Value("${oauth.tokenExpire}")
    private Long tokenExpire;
    @Value("${oauth.loginView}")
    private String loginView;
    @Value("${oauth.encryptyKey}")
    private String encryptyKey;

    @Bean
    public OAuthData initOAuthData() {
        OAuthData oAuthData = new OAuthData();
        oAuthData.setDomain(domain);
        oAuthData.setCodeExpire(codeExpire);
        oAuthData.setTokenExpire(tokenExpire);
        oAuthData.setLoginView(loginView);
        oAuthData.setEncryptyKey(encryptyKey);
        return oAuthData;
    }

}