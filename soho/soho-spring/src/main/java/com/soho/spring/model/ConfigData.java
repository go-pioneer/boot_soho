package com.soho.spring.model;


/**
 * @author shadow
 */
public class ConfigData {
    // 基本参数
    private String domain;
    private String projectCode;
    private String failureUrl;
    private String redirectUrl;
    private String jsoupPrefix;
    private String[] apiPrefix;
    // 上传参数
    private String savePath;
    private String maxFileSize;
    private String maxRequestSize;
    // 加密参数
    private String encryptyKey = "&OGFpY/6xoV`@P1Z";

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

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
    }

    public String getMaxRequestSize() {
        return maxRequestSize;
    }

    public void setMaxRequestSize(String maxRequestSize) {
        this.maxRequestSize = maxRequestSize;
    }

    public String getEncryptyKey() {
        return encryptyKey;
    }

    public void setEncryptyKey(String encryptyKey) {
        this.encryptyKey = encryptyKey;
    }

}
