package com.soho.cache.redisson.aop;

import com.alibaba.fastjson.JSON;
import com.soho.spring.cache.CacheManager;
import com.soho.spring.cache.annotation.Cache;
import com.soho.spring.cache.annotation.RDLock;
import com.soho.spring.cache.model.CacheObject;
import com.soho.spring.cache.model.CacheType;
import com.soho.spring.utils.MD5Utils;
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

import java.util.concurrent.TimeUnit;

@Aspect
@Order(40)
@Component
public class CacheAspect {

    private final static Logger log = LoggerFactory.getLogger(RDLock.class);

    @Autowired(required = false)
    private CacheManager manager;

    private final static String BIZ_CACHE = "biz_cache_";

    private static volatile com.soho.spring.cache.Cache local_cache;
    private static volatile com.soho.spring.cache.Cache remote_cache;

    @Pointcut("@annotation(cache)")
    public void serviceStatistics(Cache cache) {
    }


    @Around("serviceStatistics(cache)")
    public Object invoke(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        // long l = System.currentTimeMillis();
        if (local_cache == null) {
            local_cache = manager.getCache(CacheType.LOCAL_DATA);
        }
        if (remote_cache == null) {
            remote_cache = manager.getCache(CacheType.REMOTE_DATA);
        }
        if (local_cache == null) {
            log.error("本地缓存服务尚未初始化");
            return null;
        }
        // 获取请求参数唯一标识,hash(类名+方法名+参数)
        String className = joinPoint.getThis().toString().split("@")[0];
        String methodName = joinPoint.getSignature().getName();
        String classAndMethod = className + "." + methodName;
        StringBuffer buffer = new StringBuffer()
                .append(classAndMethod).append("; ")
                .append(joinPoint.getArgs() == null ? "" : JSON.toJSONString(joinPoint.getArgs())).append("; ");
        String hashcode = BIZ_CACHE + MD5Utils.encrypt(buffer.toString(), null);
        // 获取注解配置参数
        int local_exp = cache.local_exp();
        int remote_exp = cache.remote_exp();
        CacheObject<Object> object = local_cache.get(hashcode);
        // 本地缓存不存在数据
        if (object == null) {
            // JVM锁内存更新本地缓存
            synchronized (local_cache) {
                // 二次判断是否已有缓存结果
                object = local_cache.get(hashcode);
                if (object == null) {
                    // 获取远程缓存数据
                    object = pickRemoteCache(joinPoint, hashcode, remote_exp);
                    // 最新远程缓存数据更新到本地
                    local_cache.put(hashcode, object, local_exp);
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
                            object = pickRemoteCache(joinPoint, hashcode, remote_exp);
                            // 最新远程缓存数据更新到本地
                            local_cache.put(hashcode, object, local_exp);
                        }
                    }
                }
            }
        }
        // System.out.println("----最终测试---" + (System.currentTimeMillis() - l));
        return object.getData();
    }

    // 读取远程缓存,远程缓存不存在,则更新远程缓存
    private CacheObject<Object> pickRemoteCache(ProceedingJoinPoint joinPoint, String hashcode, int remote_exp) throws Throwable {
        if (remote_cache == null) {
            // log.warn("远程缓存服务尚未初始化");
            Object data = joinPoint.proceed();
            return new CacheObject<>(hashcode, data, remote_exp);
        }
        CacheObject<Object> object = remote_cache.get(hashcode);
        // 远程缓存没有数据
        if (object == null) {
            RedissonClient client = remote_cache.getInstance();
            RLock rLock = client.getLock(hashcode);
            try {
                // 进行排队更新远程缓存
                if (rLock.tryLock(5000, 5000, TimeUnit.MILLISECONDS)) {
                    // 二次判断是否已有缓存结果
                    object = remote_cache.get(hashcode);
                    if (object == null) {
                        Object data = joinPoint.proceed();
                        object = new CacheObject<>(hashcode, data, remote_exp);
                        remote_cache.put(hashcode, object, object.getExpire());
                        remote_cache.put(hashcode + "_", object.getLast(), object.getExpire());
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                rLock.unlock();
            }
        }
        return object;
    }

}