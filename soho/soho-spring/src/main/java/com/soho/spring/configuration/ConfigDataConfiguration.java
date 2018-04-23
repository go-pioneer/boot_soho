package com.soho.spring.configuration;

import com.soho.spring.model.ConfigData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
@Configuration
public class ConfigDataConfiguration {

    @Value("${default.domain}")
    private String domain;
    @Value("${default.projectCode}")
    private String projectCode;
    @Value("${default.apiPrefix}")
    private String apiPrefix;
    @Value("${default.jsoupPrefix}")
    private String jsoupPrefix;
    @Value("${default.failureUrl}")
    private String failureUrl;
    @Value("${default.redirectUrl}")
    private String redirectUrl;
    @Value("${default.encryptyKey}")
    private String encryptyKey;

    @Value("${upload.savePath}")
    private String savePath;
    @Value("${upload.maxFileSize}")
    private String maxFileSize;
    @Value("${upload.maxRequestSize}")
    private String maxRequestSize;

    @Bean
    public ConfigData initConfigData() {
        ConfigData config = new ConfigData();
        config.setDomain(domain);
        config.setProjectCode(projectCode);
        config.setFailureUrl(failureUrl);
        config.setRedirectUrl(redirectUrl);
        config.setJsoupPrefix(jsoupPrefix != null ? jsoupPrefix : "");
        config.setApiPrefix(apiPrefix != null ? apiPrefix.split(",") : new String[]{});
        config.setEncryptyKey(encryptyKey);
        config.setSavePath(savePath);
        config.setMaxFileSize(maxFileSize);
        config.setMaxRequestSize(maxRequestSize);
        return config;
    }

}