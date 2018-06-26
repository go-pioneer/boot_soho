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
    @Value("${default.projectCode}")
    private String projectCode;
    @Value("${default.jsoupPrefix}")
    private String jsoupPrefix;
    @Value("${default.apiPrefix}")
    private String[] apiPrefix;
    @Value("${default.projectKey}")
    private String projectKey;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

}
