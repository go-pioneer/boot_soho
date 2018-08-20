package com.soho.spring.shiro.cache;

import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.model.CacheType;
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
        defaultShiroDataCache().clear();
    }

    public V get(Object key) throws CacheException {
        return defaultShiroDataCache().get(PROKey() + key);
    }

    public V put(Object key, V value) throws CacheException {
        defaultShiroDataCache().put(PROKey() + key, value);
        return value;
    }

    public V remove(Object key) throws CacheException {
        V v = defaultShiroDataCache().get(PROKey() + key);
        defaultShiroDataCache().remove(PROKey() + key);
        return v;
    }

    public Set<Object> keys() {
        return defaultShiroDataCache().keys();
    }

    public int size() {
        return (int) defaultShiroDataCache().size();
    }

    public Collection<V> values() {
        return defaultShiroDataCache().values();
    }

    private static volatile com.soho.spring.cache.Cache cache;

    private com.soho.spring.cache.Cache defaultShiroDataCache() {
        if (cache == null) {
            cache = cacheManager.getCache(CacheType.SHIRO_DATA);
            return cache;
        }
        return cache;
    }

    private String PROKey() {
        return CacheType.SHIRO_DATA;
    }

}
