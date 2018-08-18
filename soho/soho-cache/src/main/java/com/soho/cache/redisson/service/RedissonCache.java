package com.soho.cache.redisson.service;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.imp.AbstractCache;
import com.soho.spring.utils.SpringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author shadow
 */
public class RedissonCache extends AbstractCache implements Cache {

    private RedissonClient client;

    private RedissonClient getClient() {
        if (client == null) {
            client = SpringUtils.getBean(RedissonClient.class);
            return client;
        }
        return client;
    }

    @Override
    public <V> V doGet(Object key) {
        RBucket<V> bucket = getClient().getBucket(key.toString());
        return bucket.get();
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) {
        RBucket<V> bucket = getClient().getBucket(key.toString());
        bucket.set(value, exp, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) {
        return doPut(key, value, 0);
    }

    @Override
    public <V> boolean doRemove(Object key) {
        RBucket<V> bucket = getClient().getBucket(key.toString());
        return bucket.delete();
    }

}
