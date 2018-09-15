package com.soho.cache.redisson.aop;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.aspect.DefaultAspect;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.annotation.MultiCache;
import com.soho.spring.cache.annotation.RDLock;
import com.soho.spring.cache.model.CacheObject;
import com.soho.spring.cache.model.CacheType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Aspect
@Order(40)
@Component
public class MultiCacheAspect extends DefaultAspect {

    private final static Logger log = LoggerFactory.getLogger(RDLock.class);

    @Autowired(required = false)
    private CacheManager manager;

    private static volatile com.soho.spring.cache.Cache local_cache;
    private static volatile com.soho.spring.cache.Cache remote_cache;

    @Pointcut("@annotation(cache)")
    public void serviceStatistics(MultiCache cache) {
    }


    @Around("serviceStatistics(cache)")
    public Object invoke(ProceedingJoinPoint joinPoint, MultiCache cache) throws Throwable {
        boolean local_enabel = cache.local_enable();
        boolean remote_enable = cache.remote_enable();
        // 任何级别缓存不开启直接跳过
        if (!local_enabel && !remote_enable) {
            return joinPoint.proceed();
        }
        if (local_cache == null) {
            local_cache = manager.getCache(CacheType.LOCAL_DATA);
        }
        if (remote_cache == null) {
            remote_cache = manager.getCache(CacheType.REMOTE_DATA);
        }
        if (local_enabel && local_cache == null) {
            log.error("本地缓存服务尚未初始化");
            return null;
        }
        if (remote_enable && remote_cache == null) {
            log.error("远程缓存服务尚未初始化");
            return null;
        }
        String hashcode = getReqID(joinPoint, cache.prefix());
        Object object = null;
        // 获取注解配置参数
        if (local_enabel && remote_enable) { // 开启本地,开启远程
            object = openLocalAndRemote(joinPoint, cache, hashcode);
        } else if (local_enabel && !remote_enable) { // 开启本地,关闭远程
            object = openLocalOnly(joinPoint, cache, hashcode);
        } else if (!local_enabel && remote_enable) {// 关闭本地,开启远程
            object = openRemoteOnly(joinPoint, cache, hashcode);
        }
        return object;
    }

    private Object openLocalOnly(ProceedingJoinPoint joinPoint, MultiCache cache, String hashcode) throws Throwable {
        CacheObject<Object> object = local_cache.get(hashcode);
        // 本地缓存不存在数据
        if (object == null) {
            // JVM锁内存更新本地缓存
            synchronized (local_cache) {
                // 二次判断是否已有缓存结果
                object = local_cache.get(hashcode);
                if (object == null) {
                    // 获取业务处理结果数据
                    Object data = joinPoint.proceed();
                    object = new CacheObject<>(hashcode, data, cache.local_exp());
                    // 最新数据缓存数据更新到本地
                    local_cache.put(hashcode, object, cache.local_exp());
                }
            }
        }
        return object.getData();
    }

    private Object openRemoteOnly(ProceedingJoinPoint joinPoint, MultiCache cache, String hashcode) throws Throwable {
        CacheObject<Object> object = remote_cache.get(hashcode);
        // 远程缓存没有数据
        if (object == null) {
            RedissonClient client = remote_cache.getInstance();
            String lock_str = StringUtils.isEmpty(cache.spel()) ? hashcode : (hashcode + "_" + getSpelValue(cache.spel(), joinPoint));
            RLock rLock = client.getLock(hashcode);
            try {
                // 进行排队更新远程缓存
                if (rLock.tryLock(cache.lock_timeout(), cache.lock_timeout(), TimeUnit.MILLISECONDS)) {
                    // 二次判断是否已有缓存结果
                    object = remote_cache.get(hashcode);
                    if (object == null) {
                        Object data = joinPoint.proceed();
                        object = new CacheObject<>(hashcode, data, cache.remote_exp());
                        remote_cache.put(hashcode, object, object.getExpire());
                        remote_cache.put(hashcode + "_", object.getLast(), object.getExpire());
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                rLock.unlock();
            }
        }
        return object.getData();
    }

    private Object openLocalAndRemote(ProceedingJoinPoint joinPoint, MultiCache cache, String hashcode) throws Throwable {
        CacheObject<Object> object = local_cache.get(hashcode);
        // 本地缓存不存在数据
        if (object == null) {
            // JVM锁内存更新本地缓存
            synchronized (local_cache) {
                // 二次判断是否已有缓存结果
                object = local_cache.get(hashcode);
                if (object == null) {
                    // 获取远程缓存数据
                    object = pickRemoteCache(joinPoint, cache, hashcode);
                    // 最新远程缓存数据更新到本地
                    local_cache.put(hashcode, object, cache.local_exp());
                }
            }
        } else {
            if (remote_cache != null) {
                // 获取远程缓存的最后更新时间
                Long last = remote_cache.get(hashcode + "_");
                // 判断远程缓存时间 > 本地缓存时间
                if (last == null || last <= 0 || last > object.getLast()) {
                    // JVM锁内存更新本地缓存
                    synchronized (local_cache) {
                        // 二次判断是否已有缓存结果
                        object = local_cache.get(hashcode);
                        // 判断远程缓存时间 > 本地缓存时间
                        if (last == null || last <= 0 || last > object.getLast()) {
                            object = pickRemoteCache(joinPoint, cache, hashcode);
                            // 最新远程缓存数据更新到本地
                            local_cache.put(hashcode, object, cache.local_exp());
                        }
                    }
                }
            }
        }
        return object.getData();
    }

    // 读取远程缓存,远程缓存不存在,则更新远程缓存
    private CacheObject<Object> pickRemoteCache(ProceedingJoinPoint joinPoint, MultiCache cache, String hashcode) throws Throwable {
        if (remote_cache == null) {
            log.error("远程缓存服务尚未初始化");
            Object data = joinPoint.proceed();
            return new CacheObject<>(hashcode, data, cache.remote_exp());
        }
        CacheObject<Object> object = remote_cache.get(hashcode);
        // 远程缓存没有数据
        if (object == null) {
            RedissonClient client = remote_cache.getInstance();
            String lock_str = StringUtils.isEmpty(cache.spel()) ? hashcode : (hashcode + "_" + getSpelValue(cache.spel(), joinPoint));
            RLock rLock = client.getLock(lock_str);
            try {
                // 进行排队更新远程缓存
                if (rLock.tryLock(cache.lock_timeout(), cache.lock_timeout(), TimeUnit.MILLISECONDS)) {
                    // 二次判断是否已有缓存结果
                    object = remote_cache.get(hashcode);
                    if (object == null) {
                        Object data = joinPoint.proceed();
                        object = new CacheObject<>(hashcode, data, cache.remote_exp());
                        remote_cache.put(hashcode, object, object.getExpire());
                        remote_cache.put(hashcode + "_", object.getLast(), object.getExpire());
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                rLock.unlock();
            }
        }
        return object;
    }

}