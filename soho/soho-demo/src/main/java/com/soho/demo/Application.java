package com.soho.demo;

import com.soho.cache.redisson.core.RedissonCache;
import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.CacheType;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.extend.JarServletInitializer;
import com.soho.spring.mvc.model.FastMap;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shadow
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.soho"})
public class Application extends JarServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        String[] filePath = new String[]{"classpath:application.properties", "classpath:database.properties"};
        return super.initPropertyPlaceholderConfigurer(filePath); // 本地启动方式
    }

    @Bean
    public CacheManager initCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCacheMap(new FastMap<Cache>()
                .add(CacheType.SHIRO_DATA_CACHE, EHCACHE()).add(CacheType.DEFAULT_DATA_CACHE, EHCACHE())
                .done());
        return simpleCacheManager;
    }

    @Bean
    public Cache REDISSONCACHE() {
        return new RedissonCache();
    }

    @Bean
    public Cache EHCACHE() {
        return new EhCache();
    }

}