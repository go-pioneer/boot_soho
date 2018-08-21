package com.soho.spring.rabbitmq.aop;

import com.soho.spring.rabbitmq.annotation.RabbiiMQ;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

@Aspect
@Order(50)
@Component
public class RabbiiMQAspect {

    @Pointcut("@annotation(rabbiiMQ)")
    public void serviceStatistics(RabbiiMQ rabbiiMQ) {
    }

    @Around("serviceStatistics(rabbiiMQ)")
    public Object invoke(ProceedingJoinPoint joinPoint, RabbiiMQ rabbiiMQ) throws Throwable {
        try {
            String channel = rabbiiMQ.channel();
            String key = rabbiiMQ.key();
            boolean remove = rabbiiMQ.remove();
            if (StringUtils.isEmpty(channel) || StringUtils.isEmpty(key)) {
                return joinPoint.proceed();
            }
            Object object = joinPoint.proceed();
            if (!(object instanceof Map)) {
                return object;
            }
            Map map = (Map) object;
            if (!map.containsKey(key)) {
                return object;
            }
            Object data = map.get(key);
            if (data != null) {
                // TODO 发送data数据到MQ通道-channel
            }
            if (remove) {
                map.remove(key);
            }
            return map;
        } catch (Exception e) {
            throw e;
        }

    }

}