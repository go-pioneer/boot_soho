package com.soho.spring.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Shiro缓存管理器实现类
 *
 * @author shadow
 */
@Component
public class SimpleShiroCacheManager implements CacheManager, Destroyable {

    @Autowired(required = false)
    private Cache<Object, Object> cache;

    public void destroy() throws Exception {
    }

    public Cache<Object, Object> getCache(String name) throws CacheException {
        return cache;
    }

    public Cache<Object, Object> getCache() {
        return cache;
    }

    public void setCache(Cache<Object, Object> cache) {
        this.cache = cache;
    }

}
