package com.soho.spring.model;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
public class OSSConfig {

    @Value("${alioss.appId}")
    private String appId;
    @Value("${alioss.appKey}")
    private String appKey;
    @Value("${alioss.domain}")
    private String domain;
    @Value("${alioss.endpoint}")
    private String endpoint;
    @Value("${alioss.bucketName}")
    private String bucketName;
    @Value("${alioss.savePath}")
    private String savePath;
    @Value("${alioss.maxFileSize}")
    private String maxFileSize;
    @Value("${alioss.maxRequestSize}")
    private String maxRequestSize;
    @Value("${alioss.ggkPath}")
    private String ggkPath;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
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

    public String getGgkPath() {
        return ggkPath;
    }

    public void setGgkPath(String ggkPath) {
        this.ggkPath = ggkPath;
    }

}
