package com.soho.dubbo;

import com.alibaba.dubbo.config.ProviderConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author shadow
 */
public class ProviderConfiguration {

    @Autowired(required = false)
    private DubboConfig dubboConfig;

    public ProviderConfig providerConfig() {
        ProviderConfig config = new ProviderConfig();
        config.setTimeout(dubboConfig.getTimeout());
        config.setRetries(dubboConfig.getRetries());
        config.setFilter(dubboConfig.getFilter());
        return config;
    }

}