package com.soho.cache.redisson.core;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.imp.AbstractCache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author shadow
 */
public class RedissonCache extends AbstractCache implements Cache {

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Override
    public <V> V doGet(Object key) {
        long l = System.currentTimeMillis();
        RBucket<byte[]> bucket = redissonClient.getBucket(key.toString());
        byte[] bytes = bucket.get();
        V v = (V) SerializationUtils.deserialize(bytes);
        System.out.println("read cache: " + (System.currentTimeMillis() - l));
        return v;
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) {
        long l = System.currentTimeMillis();
        RBucket<byte[]> bucket = redissonClient.getBucket(key.toString());
        byte[] bytes = SerializationUtils.serialize(value);
        if (exp == -100) {
            bucket.set(bytes);
        } else {
            bucket.set(bytes, exp, TimeUnit.SECONDS);
        }
        System.out.println("put cache: " + (System.currentTimeMillis() - l));
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) {
        return doPut(key, value, -100);
    }

    @Override
    public <V> boolean doRemove(Object key) {
        RBucket<byte[]> bucket = redissonClient.getBucket(key.toString());
        return bucket.delete();
    }

    @Override
    public RedissonClient getInstance() {
        return redissonClient;
    }

    @Override
    public Class<RedissonClient> getInstanceClassType() {
        return RedissonClient.class;
    }

}
