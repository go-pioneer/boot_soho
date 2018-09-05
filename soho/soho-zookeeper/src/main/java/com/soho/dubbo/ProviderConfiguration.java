package com.soho.dubbo;

import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shadow
 */
public class ProviderConfiguration {

    @Autowired(required = false)
    private DubboConfig dubboConfig;

    public ProviderConfig providerConfig() {
        ProviderConfig config = new ProviderConfig();
        config.setTimeout(dubboConfig.getTimeout());
        config.setVersion(dubboConfig.getVersion());
        config.setRetries(dubboConfig.getRetries());
        config.setFilter(dubboConfig.getFilter());
        return config;
    }

}