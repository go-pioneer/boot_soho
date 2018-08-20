package com.soho.spring.model;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
public class DeftConfig {

    @Value("${default.domain}")
    private String domain;
    @Value("${default.homePage}")
    private String homePage;
    @Value("${default.projectCode}")
    private String projectCode;
    @Value("${default.jsoupPrefix}")
    private String jsoupPrefix;
    @Value("${default.apiPrefix}")
    private String[] apiPrefix;
    @Value("${default.projectKey}")
    private String projectKey;
    @Value("${default.staticPrefix}")
    private String staticPrefix;
    @Value("${default.isHttps}")
    private boolean isHttps;
    @Value("${default.expireSession}")
    private int expireSession;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getJsoupPrefix() {
        return jsoupPrefix;
    }

    public void setJsoupPrefix(String jsoupPrefix) {
        this.jsoupPrefix = jsoupPrefix;
    }

    public String[] getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String[] apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getStaticPrefix() {
        return staticPrefix;
    }

    public void setStaticPrefix(String staticPrefix) {
        this.staticPrefix = staticPrefix;
    }

    public boolean isHttps() {
        return isHttps;
    }

    public void setHttps(boolean https) {
        isHttps = https;
    }

    public int getExpireSession() {
        return expireSession;
    }

    public void setExpireSession(int expireSession) {
        this.expireSession = expireSession;
    }

}
