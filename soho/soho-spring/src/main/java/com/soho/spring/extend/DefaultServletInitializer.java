package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class DefaultServletInitializer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(getClass());
    }

    public PropertyPlaceholderConfigurer initDefaultPropertyPlaceholderConfigurer() {
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        PropertyPlaceholderConfigurer placeholderConfigurer = new DefaultPropertyConfigurer(decodeKeys);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:application.properties");
        placeholderConfigurer.setLocation(resource);
        return placeholderConfigurer;
    }

    public PropertyPlaceholderConfigurer initZookeeperPropertyPlaceholderConfigurer() {
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        PropertyPlaceholderConfigurer placeholderConfigurer = new DefaultPropertyConfigurer(decodeKeys);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:application.properties");
        placeholderConfigurer.setLocation(resource);
        return placeholderConfigurer;
    }

    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        return initDefaultPropertyPlaceholderConfigurer();
    }

    public HttpMessageConverters initFastjsonHttpMessageConverter() {
        return new HttpMessageConverters(new FastjsonMessageConver());
    }

    public CacheManager initCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        Cache cache = new EhCache();
        Map<String, Cache> cacheMap = new HashMap<>();
        cacheMap.put(CacheManager.SHIRO_DATA_CACHE, cache);
        simpleCacheManager.setCacheMap(cacheMap);
        return simpleCacheManager;
    }

}
