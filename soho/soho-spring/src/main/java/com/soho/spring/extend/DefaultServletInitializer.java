package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.model.CacheType;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.mvc.model.FastMap;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author shadow
 */
public class DefaultServletInitializer extends SpringBootServletInitializer {

    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(getClass());
    }

    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer(String[] filePath, String[] decodeKeys) {
        if (filePath == null || filePath.length <= 0) {
            filePath = new String[]{"classpath:application.properties"};
        }
        if (decodeKeys == null || decodeKeys.length == 0) {
            decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        }
        return new DefaultPropertyConfigurer(filePath, decodeKeys);
    }

    public CacheManager initCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCacheMap(new FastMap<Cache>()
                .add(CacheType.SHIRO_DATA, new EhCache()).add(CacheType.DEFAULT_DATA, new EhCache())
                .done());
        return simpleCacheManager;
    }

}
