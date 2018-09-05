package com.soho.dubbo;

import com.alibaba.dubbo.config.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
@Configuration
public class ConsumerConfiguration {

    @Autowired(required = false)
    private DubboConfig dubboConfig;

    @Bean
    public ConsumerConfig consumerConfig() {
        ConsumerConfig config = new ConsumerConfig();
        config.setCheck(dubboConfig.getCheck());
        config.setTimeout(dubboConfig.getTimeout());
        config.setVersion(dubboConfig.getVersion());
        config.setRetries(dubboConfig.getRetries());
        return config;
    }

}