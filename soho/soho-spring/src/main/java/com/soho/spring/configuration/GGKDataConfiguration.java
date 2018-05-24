package com.soho.spring.configuration;

import com.soho.spring.model.GGKData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
        if (StringUtils.isEmpty(urls)) {
            config.setUrls(new ArrayList<>());
        } else {
            String[] strs = urls.replaceAll(" ", "").split(",");
            config.setUrls(Arrays.asList(strs));
        }
        return config;
    }

}