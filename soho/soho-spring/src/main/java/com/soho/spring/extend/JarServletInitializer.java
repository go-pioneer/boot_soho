package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.mvc.model.FastMap;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author shadow
 */
public class JarServletInitializer extends SpringBootServletInitializer {

    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer(String[] filePath, String[] decodeKeys) {
        if (decodeKeys == null) {
            decodeKeys = new String[]{};
        }
        return new DefaultPropertyConfigurer(filePath, decodeKeys);
    }

    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer(String[] filePath) {
        return new DefaultPropertyConfigurer(filePath, new String[]{});
    }

    public CacheManager initCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCacheMap(new FastMap<Cache>()
                .add(CacheManager.SHIRO_DATA_CACHE, new EhCache()).add(CacheManager.DEFAULT_CACHE, new EhCache())
                .done());
        return simpleCacheManager;
    }

}
