package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import com.soho.spring.model.DeftConfig;
import com.soho.spring.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;

/**
 * @author shadow
 */
public abstract class AbstractCache implements Cache {

    @Autowired(required = false)
    private volatile DeftConfig deftConfig;

    private String cacheName;

    public AbstractCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public <V> V get(Object key) {
        try {
            return doGet(reBuildKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <V> boolean put(Object key, V value, int exp) {
        try {
            return doPut(reBuildKey(key), value, exp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public <V> boolean put(Object key, V value) {
        try {
            return doPut(reBuildKey(key), value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(Object key) {
        try {
            return doRemove(reBuildKey(key));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public abstract <V> V doGet(Object key) throws Exception;

    public abstract <V> boolean doPut(Object key, V value, int exp) throws Exception;

    public abstract <V> boolean doPut(Object key, V value) throws Exception;

    public abstract <V> boolean doRemove(Object key) throws Exception;

    @Override
    public long size() {
        return 0;
    }

    @Override
    public Set<Object> keys() {
        return null;
    }

    public void clear() {
    }

    @Override
    public <V> Collection<V> values() {
        return null;
    }

    @Override
    public <V> V getInstance() {
        return null;
    }

    @Override
    public <V> Class<V> getInstanceClassType() {
        return null;
    }

    private String reBuildKey(Object key) {
        if (deftConfig == null) {
            deftConfig = SpringUtils.getBean(DeftConfig.class);
        }
        return deftConfig.getProjectCode() + cacheName + key;
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

}
