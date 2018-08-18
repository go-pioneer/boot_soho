package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.util.StringUtils;

/**
 * @author shadow
 */
public class EhCache extends AbstractCache implements Cache {

    private static CacheManager cacheManager = null;
    private String cacheName = "soho";
    private String xmlPath = "/ehcache.xml";

    public EhCache() {
        this(null, null);
    }

    public EhCache(String xmlPath, String cacheName) {
        if (!StringUtils.isEmpty(cacheName)) {
            this.cacheName = cacheName;
        }
        if (!StringUtils.isEmpty(xmlPath)) {
            this.xmlPath = xmlPath;
        }
        cacheManager = CacheManager.create(getClass().getResource(this.xmlPath));
    }

    @Override
    public <V> V doGet(Object key) {
        Element element = getEhCache().get(key);
        if (element == null || element.getObjectValue() == null) {
            return null;
        }
        return (V) element.getObjectValue();
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) {
        Element element = new Element(key, value);
        element.setTimeToLive(exp);
        getEhCache().put(element);
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) {
        Element element = new Element(key, value);
        getEhCache().put(element);
        return true;
    }

    @Override
    public <V> boolean doRemove(Object key) {
        getEhCache().remove(key);
        return true;
    }

    private net.sf.ehcache.Cache getEhCache() {
        return cacheManager.getCache(this.cacheName);
    }

}
