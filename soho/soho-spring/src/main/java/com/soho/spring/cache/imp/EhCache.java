package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.util.Collection;
import java.util.Set;

public class EhCache implements Cache {

    private static CacheManager cacheManager = null;

    public EhCache() {
        cacheManager = CacheManager.create(getClass().getResource("/ehcache.xml"));
    }

    @Override
    public <V> V get(Object key) {
        try {
            Element element = getCache().get(key);
            return (V) element.getObjectValue();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <V> boolean put(Object key, V value, int exp) {
        try {
            Element element = new Element(key, value);
            element.setTimeToLive(exp);
            getCache().put(element);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public <V> boolean put(Object key, V value) {
        try {
            Element element = new Element(key, value);
            element.setTimeToLive(0);
            getCache().put(element);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean remove(Object key) {
        try {
            cacheManager.getCache("data").remove(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public Set<Object> keys() {
        return null;
    }

    @Override
    public <V> Collection<V> values() {
        return null;
    }

    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    public Class<?> getInstanceClassType() {
        return null;
    }

    private net.sf.ehcache.Cache getCache() {
        return cacheManager.getCache("cache");
    }

}
