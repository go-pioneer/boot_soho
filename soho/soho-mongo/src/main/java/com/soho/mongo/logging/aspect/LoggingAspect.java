package com.soho.mongo.logging.aspect;

import com.alibaba.fastjson.JSON;
import com.soho.mongo.logging.Logging;
import com.soho.spring.extend.collection.FastMap;
import com.soho.spring.utils.HttpUtils;
import com.soho.spring.utils.SpringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;

@Aspect
@Order(-999)
@Component
public class LoggingAspect {

    private final static Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    @Pointcut("@annotation(logging)")
    public void serviceStatistics(Logging logging) {
    }

    @Around("serviceStatistics(logging)")
    public Object invoke(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        try {
            long begin = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug(JSON.toJSONString(logMessage(joinPoint, logging, result, begin, System.currentTimeMillis())));
            }
            if (logging.mongo() && !StringUtils.isEmpty(logging.collection())) {
                if (mongoTemplate == null) {
                    mongoTemplate = SpringUtils.getBean(MongoTemplate.class);
                }
                mongoTemplate.save(logMessage(joinPoint, logging, result, begin, System.currentTimeMillis()), logging.collection());
            }
            if (result instanceof Map && logging.delkeys() != null) {
                Map newResult = (Map) result;
                for (String key : logging.delkeys()) {
                    if (!StringUtils.isEmpty(key) && newResult.get(key) != null) {
                        newResult.remove(key);
                    }
                }
                return newResult;
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    private Map logMessage(ProceedingJoinPoint joinPoint, Logging logging, Object result, long begin, long end) {
        String module = logging.module();
        String className = joinPoint.getThis().toString().split("@")[0];
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        return new FastMap<>()
                .add("beginTime", begin)
                .add("endTime", end)
                .add("module", module == null ? "" : module)
                .add("className", className)
                .add("methodName", methodName)
                .add("methodArgs", args == null ? new ArrayList<>() : args)
                .add("result", result)
                .add("costTime", end - begin)
                .add("hostIp", HttpUtils.getHostIp())
                .done();
    }

}