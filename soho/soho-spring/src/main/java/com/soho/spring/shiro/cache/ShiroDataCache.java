package com.soho.spring.shiro.cache;

import com.soho.spring.cache.CacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

/**
 * Shiro缓存实现类
 *
 * @author shadow
 */
@Component
public class ShiroDataCache<K, V> implements Cache<Object, V> {

    @Autowired(required = false)
    private CacheManager cacheManager;

    public void clear() throws CacheException {
        getCache().clear();
    }

    public V get(Object key) throws CacheException {
        return getCache().get(getCacheKey() + key);
    }

    public V put(Object key, V value) throws CacheException {
        getCache().put(getCacheKey() + key, value);
        return value;
    }

    public V remove(Object key) throws CacheException {
        V v = getCache().get(getCacheKey() + key);
        getCache().remove(getCacheKey() + key);
        return v;
    }

    public Set<Object> keys() {
        return getCache().keys();
    }

    public int size() {
        return (int) getCache().size();
    }

    public Collection<V> values() {
        return getCache().values();
    }

    private com.soho.spring.cache.Cache getCache() {
        com.soho.spring.cache.Cache cache = cacheManager.getCache(CacheManager.SHIRO_DATA_CACHE);
        if (cache == null) {
            System.out.println("cache is null");
        }
        return cache;
    }

    private String getCacheKey() {
        return CacheManager.SHIRO_DATA_CACHE;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

}
