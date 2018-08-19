package com.soho.cache.redisson.core;

import com.soho.spring.model.RedissonConfig;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@Configuration
public class RedissonConfiguration {

    private final static Logger log = LoggerFactory.getLogger(RedissonConfiguration.class);

    @Autowired(required = false)
    private RedissonConfig redissonConfig;

    @Bean
    public RedissonClient initRedissonClient() {
        try {
            String address = "redis://" + redissonConfig.getHost() + ":" + redissonConfig.getPort();
            Config config = new Config().setCodec(new ByteArrayCodec());
            config.useSingleServer()
                    // redis访问地址
                    .setAddress(address)
                    .setPassword(StringUtils.isEmpty(redissonConfig.getPassword()) ? null : redissonConfig.getPassword());
                /*// 连接池中连接数最大为500
                .setConnectionPoolSize(500)
                // 当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
                .setIdleConnectionTimeout(10000)
                // 同任何节点建立连接时的等待超时。时间单位是毫秒。
                .setConnectTimeout(30000)
                // 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                .setTimeout(3000)
                .setPingTimeout(30000);*/

            // 添加主从配置
            // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
            return Redisson.create(config);
        } catch (Exception e) {
            log.error("Redisson初始化失败: " + e.getMessage());
            return null;
        }
    }

}
