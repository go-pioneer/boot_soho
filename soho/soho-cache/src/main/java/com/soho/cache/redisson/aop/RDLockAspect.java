package com.soho.cache.redisson.aop;

import com.soho.cache.redisson.lock.RDLock;
import com.soho.cache.redisson.service.RedissonService;
import com.soho.spring.shiro.utils.SessionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component
public class RDLockAspect {

    @Autowired
    private RedissonService redissonService;

    @Pointcut("@annotation(drLock)")
    public void serviceStatistics(RDLock drLock) {
    }


    @Around("serviceStatistics(rdLock)")
    public Object invoke(ProceedingJoinPoint joinPoint, RDLock rdLock) throws Throwable {
        String key = rdLock.key();
        String exkey = rdLock.exkey();
        if (!StringUtils.isEmpty(exkey)) {
            key = key + "_" + exkey;
        }
        if (rdLock.user()) {
            Long userId = SessionUtils.getUserId();
            key = key + "_" + (userId == null ? 0 : userId);
        }
        RLock lock = redissonService.getRLock(key);
        try {
            if (lock.tryLock(rdLock.waitime(), rdLock.timeout(), rdLock.unit())) {
                return joinPoint.proceed();
            } else {
                Thread.sleep(rdLock.sleep());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            lock.unlock();
        }
        return null;
    }

}