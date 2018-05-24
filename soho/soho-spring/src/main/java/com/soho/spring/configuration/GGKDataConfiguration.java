package com.soho.spring.configuration;

import com.soho.spring.model.GGKData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
@Configuration
public class GGKDataConfiguration {

    @Value("${ggk.appId}")
    private String appId;
    @Value("${ggk.appKey}")
    private String appKey;
    @Value("${ggk.path}")
    private String path;
    @Value("${ggk.urls}")
    private String urls;

    @Bean
    public GGKData initGGKData() {
        GGKData config = new GGKData();
        config.setAppId(appId);
        config.setAppKey(appKey);
        config.setPath(path);
        config.setUrls(urls);
        return config;
    }

}