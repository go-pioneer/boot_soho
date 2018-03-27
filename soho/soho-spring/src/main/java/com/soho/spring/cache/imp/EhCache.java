package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author shadow
 */
public class EhCache extends AbstractCache implements Cache {

    private static CacheManager cacheManager = null;

    public EhCache() {
        cacheManager = CacheManager.create(getClass().getResource("/ehcache.xml"));
    }

    @Override
    public <V> V doGet(Object key) throws Exception {
        Element element = getEhCache().get(key);
        if (element == null || element.getObjectValue() == null) {
            return null;
        }
        return (V) element.getObjectValue();
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) throws Exception {
        Element element = new Element(key, value);
        element.setTimeToLive(exp);
        getEhCache().put(element);
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) throws Exception {
        Element element = new Element(key, value);
        getEhCache().put(element);
        return true;
    }

    @Override
    public <V> boolean doRemove(Object key) throws Exception {
        getEhCache().remove(key);
        return true;
    }

    private net.sf.ehcache.Cache getEhCache() {
        return cacheManager.getCache("cache");
    }

}
