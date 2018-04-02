package com.soho.cache.imp;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.imp.AbstractCache;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;

/**
 * @author shadow
 */
public class XMemcachedCache extends AbstractCache implements Cache {

    private MemcachedClient client;

    public XMemcachedCache(String servers) {
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(servers));
        try {
            this.client = builder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
