package com.soho.demo;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.extend.DecipherPropertyPlaceholderConfigurer;
import com.soho.spring.extend.FastjsonMessageConver;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.soho"})
public class Application {

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        String[] dekeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        PropertyPlaceholderConfigurer placeholderConfigurer = new DecipherPropertyPlaceholderConfigurer(dekeys);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:application.properties");
        placeholderConfigurer.setLocation(resource);
        return placeholderConfigurer;
    }

    @Bean
    public HttpMessageConverters initFastjsonHttpMessageConverter() {
        return new HttpMessageConverters(new FastjsonMessageConver());
    }

    @Bean
    public CacheManager initCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        Cache cache = new EhCache();
        Map<String, Cache> cacheMap = new HashMap<>();
        cacheMap.put(CacheManager.SHIRO_DATA_CACHE, cache);
        simpleCacheManager.setCacheMap(cacheMap);
        return simpleCacheManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}