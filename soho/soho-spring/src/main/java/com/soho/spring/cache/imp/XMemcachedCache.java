package com.soho.spring.cache.imp;

import com.soho.spring.cache.Cache;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * @author shadow
 */
public class XMemcachedCache extends AbstractCache implements Cache {

    private MemcachedClient client;

    public XMemcachedCache(MemcachedClient client) {
        this.client = client;
    }

    @Override
    public <V> V doGet(Object key) throws Exception {
        return client.get(key.toString());
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) throws Exception {
        return client.set(key.toString(), exp, value);
    }

    @Override
    public <V> boolean doPut(Object key, V value) throws Exception {
        return client.set(key.toString(), 0, value);
    }

    @Override
    public <V> boolean doRemove(Object key) throws Exception {
        return client.delete(key.toString());
    }
}
