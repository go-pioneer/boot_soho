package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.CacheManager;

import java.util.Map;

/**
 * 简易缓存管理器实现类
 *
 * @author shadow
 */
public class SimpleCacheManager implements CacheManager {

    private Map<String, Cache> cacheMap;

    public Cache getCache(String cacheName) {
        return cacheMap.get(cacheName);
    }

    public void destroy() {

    }

    public Map<String, Cache> getCacheMap() {
        return cacheMap;
    }

    public void setCacheMap(Map<String, Cache> cacheMap) {
        this.cacheMap = cacheMap;
    }

}
