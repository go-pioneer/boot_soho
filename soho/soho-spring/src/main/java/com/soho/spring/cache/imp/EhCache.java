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
        CacheManager manager = CacheManager.create(getClass().getResource(this.xmlPath));
        cache = manager.getCache(this.cacheName);
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

}
