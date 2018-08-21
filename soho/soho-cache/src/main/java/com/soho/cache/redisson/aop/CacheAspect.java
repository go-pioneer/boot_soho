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

    private static volatile com.soho.spring.cache.Cache local_cache;
    private static volatile com.soho.spring.cache.Cache remote_cache;

    private static volatile Object jvm_lock = new Object();

    @Pointcut("@annotation(cache)")
    public void serviceStatistics(Cache cache) {
    }


    @Around("serviceStatistics(cache)")
    public Object invoke(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
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
        if (remote_cache == null) {
            log.error("远程缓存服务尚未初始化");
        }
        // 获取请求参数唯一标识,hash(类名+方法名+参数)
        String className = joinPoint.getThis().toString().split("@")[0];
        String methodName = joinPoint.getSignature().getName();
        String classAndMethod = className + "." + methodName;
        StringBuffer buffer = new StringBuffer()
                .append(classAndMethod).append("; ")
                .append(joinPoint.getArgs() == null ? "" : JSON.toJSONString(joinPoint.getArgs())).append("; ");
        String hashcode = MD5Utils.encrypt(buffer.toString(), null);
        CacheObject<Object> object = local_cache.get(hashcode);
        if (object == null) { // 本地缓存不存在数据
            object = pickRemoteCache(joinPoint, hashcode, cache.remote_exp());
            System.out.println(111 + "---" + object);
            synchronized (jvm_lock) { // JVM锁内存更新本地缓存
                System.out.println(222);
                if (local_cache.get(hashcode) == null) { // 如二次判断本地缓存为空则更新
                    local_cache.put(hashcode, object, cache.local_exp());
                    System.out.println("------更新了本地数据1111");
                    System.out.println(333 + "---" + object);
                }
            }
        } else {
            if (remote_cache != null) {
                System.out.println(444);
                Long last = remote_cache.get(hashcode + "_");
                System.out.println(last + "----" + object.getLast());
                if (last == null || last <= 0 || last > object.getLast()) {
                    System.out.println(555);
                    object = pickRemoteCache(joinPoint, hashcode, cache.remote_exp());
                    synchronized (jvm_lock) {
                        System.out.println(666);
                        CacheObject<Object> object2 = local_cache.get(hashcode);
                        if (object2 == null || object2.getLast() <= last) {
                            local_cache.put(hashcode, object, cache.local_exp());
                            System.out.println("------更新了本地数据2222");
                            System.out.println(777);
                        }
                    }
                }
            }
        }
        if (object == null) {
            object = local_cache.get(hashcode);
        }
        System.out.println(object + "----888");
        return object.getData();
    }

    // 读取远程缓存,远程缓存不存在,则更新远程缓存
    private CacheObject<Object> pickRemoteCache(ProceedingJoinPoint joinPoint, String hashcode, int expire) throws Throwable {
        if (remote_cache == null) {
            Object data = joinPoint.proceed();
            return new CacheObject<>(hashcode, data, expire);
        }
        CacheObject<Object> object = remote_cache.get(hashcode);
        if (object == null) {
            RedissonClient client = remote_cache.getInstance();
            RLock rLock = client.getLock(hashcode);
            try {
                if (rLock.tryLock(0, 5, TimeUnit.SECONDS)) {
                    object = remote_cache.get("lock4cache_" + hashcode); // 二次判断是否已经有人进行缓存处理
                    if (object == null) {
                        Object data = joinPoint.proceed();
                        object = new CacheObject<>(hashcode, data, expire);
                        remote_cache.put(hashcode, object, object.getExpire());
                        remote_cache.put(hashcode + "_", object.getLast(), object.getExpire());
                    }
                    return object;
                } else {
                    Thread.sleep(50);
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