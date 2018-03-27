package com.soho.spring.shiro.initialize;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class InitDefinition {

    private String loginUrl = "/login";
    private String successUrl = "/index";
    private String unauthorizedUrl = "/unauthorized";
    private Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();

    public InitDefinition() {

    }

    public InitDefinition(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public Map<String, String> getFilterChainDefinitionMap() {
        return filterChainDefinitionMap;
    }

    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        this.filterChainDefinitionMap = filterChainDefinitionMap;
    }
}
