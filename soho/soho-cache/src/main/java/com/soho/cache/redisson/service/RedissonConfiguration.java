package com.soho.cache.redisson.service;

import com.soho.spring.model.RedissonConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfiguration {

    @Autowired
    private RedissonConfig redissonConfig;

    @Bean
    public RedissonClient initRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redissonConfig.getHost() + ":" + redissonConfig.getPort());
        // 添加主从配置
        // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
        return Redisson.create(config);
    }

}
