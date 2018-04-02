package com.soho.oauth2.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author shadow
 */
public class OAuth2LoginToken implements AuthenticationToken {

    private String code;

    public OAuth2LoginToken(String code) {
        this.code = code;
    }

    @Override
    public Object getPrincipal() {
        return this.code;
    }

    @Override
    public Object getCredentials() {
        return this.code;
    }

}
