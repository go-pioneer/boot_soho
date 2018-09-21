package com.soho.rabbitmq.aspect;

import com.soho.rabbitmq.annotation.RabbiiMQ;
import com.soho.rabbitmq.core.MQProducter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Order(999)
@Component
public class RabbitMQAspect {

    @Autowired
    private MQProducter producter;

    @Pointcut("@annotation(mq)")
    public void serviceStatistics(RabbiiMQ mq) {
    }

    @Around("serviceStatistics(mq)")
    public Object invoke(ProceedingJoinPoint joinPoint, RabbiiMQ mq) throws Throwable {
        try {
            Object object = joinPoint.proceed();
            String exchange = mq.exchange();
            String queue = mq.queue();
            String[] mapkey = mq.mapkey();
            boolean remove = mq.remove();
            long delay = mq.delay();
            if (StringUtils.isEmpty(queue) || StringUtils.isEmpty(mapkey)) {
                return object;
            }
            if (object == null || !(object instanceof Map)) {
                return object;
            }
            Map map = (Map) object;
            if (!map.isEmpty()) { // 发送Map数据到MQ队列
                Map sendData = new HashMap<>(mapkey.length);
                for (String key : mapkey) {
                    sendData.put(key, map.get(key));
                    if (remove) {
                        map.remove(key);
                    }
                }
                if (!sendData.isEmpty()) {
                    sendData.put("exchange", exchange);
                    sendData.put("queue", queue);
                    sendData.put("retries", 0);
                    sendData.put("method", joinPoint.getSignature().getName());
                    producter.send(exchange, queue, sendData, delay);
                }
            }
            return map;
        } catch (Exception e) {
            throw e;
        }
    }

}