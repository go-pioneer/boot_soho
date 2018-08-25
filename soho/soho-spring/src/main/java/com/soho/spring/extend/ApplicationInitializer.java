package com.soho.spring.extend;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.imp.SimpleCacheManager;
import com.soho.spring.mvc.model.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.util.List;

/**
 * @author shadow
 */
public class ApplicationInitializer extends SpringBootServletInitializer {

    private final static Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

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
