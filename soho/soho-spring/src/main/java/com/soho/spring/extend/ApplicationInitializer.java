package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.model.CacheType;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.mvc.model.FastMap;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.Map;

/**
 * @author shadow
 */
public class ApplicationInitializer extends SpringBootServletInitializer {

    protected PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer(String[] filePath, String[] decodeKeys) {
        if (decodeKeys == null) {
            decodeKeys = new String[]{};
        }
        return new DefaultPropertyConfigurer(filePath, decodeKeys);
    }

    protected CacheManager initCacheManager() {
        Map<String, Cache> cacheMap = new FastMap<Cache>()
                .add(CacheType.SHIRO_DATA, new EhCache()).add(CacheType.DEFAULT_DATA, new EhCache())
                .done();
        return new SimpleCacheManager(cacheMap);
    }

}
