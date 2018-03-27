package com.soho.spring.model;


/**
 * @author shadow
 */
public class ConfigData {

    private String projectCode;
    private String failureUrl;
    private String redirectUrl;
    private String[] apiPrefix;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String[] getApiPrefix() {
        return apiPrefix;
    }

    public void setApiPrefix(String[] apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

}
