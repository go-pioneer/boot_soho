package com.soho.spring.model;


/**
 * @author shadow
 */
public class ConfigData {

    private String database;
    private String projectCode;
    private String failureUrl;
    private String redirectUrl;
    private String jsoupPrefix;
    private String[] apiPrefix;

    private String encrypty_key = "wD$w9^gc6v2%v/GT";

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

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

    public String getEncrypty_key() {
        return encrypty_key;
    }

    public void setEncrypty_key(String encrypty_key) {
        this.encrypty_key = encrypty_key;
    }

}
