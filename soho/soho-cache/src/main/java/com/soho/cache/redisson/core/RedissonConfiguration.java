package com.soho.cache.redisson.core;

import com.soho.spring.model.RedissonConfig;
import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.ByteArrayCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

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
                    // 服务器地址
                    .setAddress(address)
                    // 访问密码
                    .setPassword(StringUtils.isEmpty(redissonConfig.getPassword()) ? null : redissonConfig.getPassword())
                    // 最少空闲连接数150
                    .setConnectionMinimumIdleSize(150)
                    // 连接池中连接数最大为500
                    .setConnectionPoolSize(500)
                    // 当前连接池里的连接数量超过了最小空闲连接数，而同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里去掉。时间单位是毫秒。
                    .setIdleConnectionTimeout(10000)
                    // 同任何节点建立连接时的等待超时。时间单位是毫秒。
                    .setConnectTimeout(30000)
                    // 等待节点回复命令的时间。该时间从命令发送成功时开始计时。
                    .setTimeout(3000)
                    .setKeepAlive(true)
                    .setPingTimeout(30000);

            // 添加主从配置
            // config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"",""});
            RedissonClient client = Redisson.create(config);
            log.info("Redisson正在初始化...");
            RBucket<byte[]> test = client.getBucket("test");
            test.set("test".getBytes(), 10l, TimeUnit.SECONDS);
            if (test.get() != null) {
                log.info("Redisson初始化成功...");
            }
            return client;
        } catch (Exception e) {
            log.error("Redisson初始化失败: " + e.getMessage());
            return null;
        }
    }

}
