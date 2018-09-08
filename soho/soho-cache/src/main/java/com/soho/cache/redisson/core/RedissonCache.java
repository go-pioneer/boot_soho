package com.soho.cache.redisson.core;

import com.soho.spring.cache.Cache;
import com.soho.spring.cache.imp.AbstractCache;
import com.soho.spring.cache.model.CacheObject;
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


    public RedissonCache(String cacheName) {
        super(cacheName);
    }

    @Override
    public <V> V doGet(Object key) {
        // long l = System.currentTimeMillis();
        RBucket<byte[]> bucket = redissonClient.getBucket(key.toString());
        byte[] bytes = bucket.get();
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        CacheObject<V> object = (CacheObject<V>) SerializationUtils.deserialize(bytes);
        // System.out.println("read cache: " + (System.currentTimeMillis() - l));
        return object.getData();
    }

    @Override
    public <V> boolean doPut(Object key, V value, int exp) {
        // long l = System.currentTimeMillis();
        RBucket<byte[]> bucket = redissonClient.getBucket(key.toString());
        CacheObject<V> object = new CacheObject<>(key, value, exp);
        object.setLast(System.currentTimeMillis());
        object.setVersion(object.getVersion() + 1);
        byte[] bytes = SerializationUtils.serialize(object);
        if (exp == -1) {
            bucket.set(bytes);
        } else {
            bucket.set(bytes, exp, TimeUnit.SECONDS);
        }
        // System.out.println("put cache: " + (System.currentTimeMillis() - l));
        return true;
    }

    @Override
    public <V> boolean doPut(Object key, V value) {
        return doPut(key, value, -1);
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
