package com.soho.demo;

import com.soho.cache.redisson.core.RedissonCache;
import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.model.CacheType;
import com.soho.spring.extend.ApplicationInitializer;
import com.soho.spring.extend.DefaultPropertyConfigurer;
import com.soho.spring.extend.collection.FastList;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shadow
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.dubbo.demo", "com.soho"})
public class Application extends ApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        String[] filePath = new String[]{"classpath:application.properties", "classpath:database.properties", "classpath:rabbitmq.properties", "classpath:mongo.properties"};
        return new DefaultPropertyConfigurer(filePath, new String[]{}); // 本地启动方式
    }

    @Bean
    public CacheManager initCacheManager() {
        FastList<Cache> fastList = new FastList<>().add(SHIRO_CACHE()).add(REMOTE_CACHE()).add(LOCAL_CACHE());
        return initCacheManager(fastList.done());
    }

    @Bean
    public Cache SHIRO_CACHE() {
        return new EhCache(CacheType.SHIRO_DATA);
    }

    @Bean
    public Cache REMOTE_CACHE() {
        return new RedissonCache(CacheType.REMOTE_DATA);
    }

    @Bean
    public Cache LOCAL_CACHE() {
        return new EhCache(CacheType.LOCAL_DATA);
    }

}