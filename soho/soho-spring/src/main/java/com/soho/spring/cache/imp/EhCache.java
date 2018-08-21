package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.model.CacheObject;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.shiro.session.Session;
import org.springframework.util.StringUtils;

/**
 * @author shadow
 */
public class EhCache extends AbstractCache implements Cache {

    private volatile static net.sf.ehcache.Cache cache = null;
    private String localCacheName = "soho";
    private String xmlPath = "/ehcache.xml";

    public EhCache(String cacheName) {
        this(cacheName, null, null);
    }

    public EhCache(String cacheName, String xmlPath, String localCacheName) {
        super(cacheName);
        if (!StringUtils.isEmpty(localCacheName)) {
            this.localCacheName = localCacheName;
        }
        if (!StringUtils.isEmpty(xmlPath)) {
            this.xmlPath = xmlPath;
        }
        CacheManager manager = CacheManager.create(getClass().getResource(this.xmlPath));
        cache = manager.getCache(this.localCacheName);
    }

    @Override
    public <V> V doGet(Object key) {
        Element element = cache.get(key);
        if (element == null || element.getObjectValue() == null) {
            return null;
        }
        return ((CacheObject<V>) element.getObjectValue()).getData();
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) {
        if (value instanceof Session) {
            Session session = (Session) value;
            exp = (int) session.getTimeout() / 1000;
        }
        CacheObject<V> object = new CacheObject<>(key, value, exp);
        object.setLast(System.currentTimeMillis());
        object.setVersion(object.getVersion() + 1);
        Element element = new Element(key, object);
        if (exp != -1) {
            element.setTimeToLive(exp);
        }
        cache.put(element);
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) {
        return doPut(key, value, -1);
    }

    @Override
    public <V> boolean doRemove(Object key) {
        cache.remove(key);
        return true;
    }

    @Override
    public net.sf.ehcache.Cache getInstance() {
        return cache;
    }

    @Override
    public Class<net.sf.ehcache.Cache> getInstanceClassType() {
        return net.sf.ehcache.Cache.class;
    }

}
