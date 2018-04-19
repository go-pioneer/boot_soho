package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class DefaultServletInitializer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(getClass());
    }

    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        String filePath = "classpath:application.properties";
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        return new DefaultPropertyConfigurer(filePath, decodeKeys);
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
