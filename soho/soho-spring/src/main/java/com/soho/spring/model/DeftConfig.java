package com.soho.spring.model;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
@ConfigurationProperties(prefix = "default")
public class DeftConfig {
    // 基本参数
    private String domain;
    private String projectCode;
    private String failureUrl;
    private String redirectUrl;
    private String jsoupPrefix;
    private String[] apiPrefix;
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

    public String getEncryptyKey() {
        return encryptyKey;
    }

    public void setEncryptyKey(String encryptyKey) {
        this.encryptyKey = encryptyKey;
    }

}
