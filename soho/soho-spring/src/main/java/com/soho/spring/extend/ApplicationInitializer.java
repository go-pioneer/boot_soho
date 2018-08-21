package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.model.CacheType;
import com.soho.spring.cache.imp.EhCache;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.mvc.model.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
public class ApplicationInitializer extends SpringBootServletInitializer {

    private final static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    protected PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer(String[] filePath, String[] decodeKeys) {
        if (decodeKeys == null) {
            decodeKeys = new String[]{};
        }
        return new DefaultPropertyConfigurer(filePath, decodeKeys);
    }

    protected CacheManager initCacheManager(List<Cache> caches) {
        if (caches == null || caches.isEmpty()) {
            log.error("缓存服务初始化失败: 请设置相应缓存堆");
            System.exit(-1);
        }
        FastMap<Cache> fastMap = new FastMap<>();
        for (Cache cache : caches) {
            fastMap.add(cache.getCacheName(), cache);
        }
        return new SimpleCacheManager(fastMap.done());
    }

}
