package com.soho.spring.configuration;

import com.soho.spring.model.OSSData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
@Configuration
public class OSSDataConfiguration {

    @Value("${oss.appId}")
    private String appId;
    @Value("${oss.appKey}")
    private String appKey;
    @Value("${oss.domain}")
    private String domain;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.savePath}")
    private String savePath;
    @Value("${oss.maxFileSize}")
    private String maxFileSize;
    @Value("${oss.maxRequestSize}")
    private String maxRequestSize;

    @Bean
    public OSSData initOSSData() {
        OSSData config = new OSSData();
        config.setAppId(appId);
        config.setAppKey(appKey);
        config.setDomain(domain);
        config.setEndpoint(endpoint);
        config.setBucketName(bucketName);
        config.setSavePath(savePath);
        config.setMaxFileSize(maxFileSize);
        config.setMaxRequestSize(maxRequestSize);
        return config;
    }

}