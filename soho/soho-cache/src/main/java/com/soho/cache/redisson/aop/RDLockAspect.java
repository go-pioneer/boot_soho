package com.soho.cache.redisson.aop;

import com.soho.spring.aspect.DefaultAspect;
import com.soho.spring.cache.annotation.RDLock;
import com.soho.spring.shiro.utils.SessionUtils;
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

@Aspect
@Order(10)
@Component
public class RDLockAspect extends DefaultAspect {

    private final static Logger log = LoggerFactory.getLogger(RDLock.class);

    @Autowired(required = false)
    private RedissonClient redissonClient;

    @Pointcut("@annotation(drLock)")
    public void serviceStatistics(RDLock drLock) {
    }


    @Around("serviceStatistics(rdLock)")
    public Object invoke(ProceedingJoinPoint joinPoint, RDLock rdLock) throws Throwable {
        if (redissonClient == null) {
            log.error("Redisson服务尚未初始化");
            return joinPoint.proceed();
        }
        String prefix = rdLock.prefix();
        String spel = rdLock.spel();
        if (!StringUtils.isEmpty(spel)) {
            String value = getSpelValue(spel, joinPoint);
            prefix = prefix + "_" + value;
        }
        if (rdLock.user()) {
            Long userId = SessionUtils.getUserId();
            prefix = prefix + "_" + (userId == null ? 0 : userId);
        }
        RLock rLock = redissonClient.getLock(prefix);
        try {
            if (rLock.tryLock(rdLock.waitime(), rdLock.timeout(), rdLock.unit())) {
                return joinPoint.proceed();
            } else {
                Thread.sleep(rdLock.sleep());
            }
        } catch (Exception e) {
            throw e;
        } finally {
            rLock.unlock();
        }
        return null;
    }

}