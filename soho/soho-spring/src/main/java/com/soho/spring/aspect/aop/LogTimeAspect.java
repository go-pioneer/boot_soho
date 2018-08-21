package com.soho.spring.aspect.aop;

import com.alibaba.fastjson.JSON;
import com.soho.spring.aspect.annotation.LogTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogTimeAspect {

    private static Logger log = LoggerFactory.getLogger(LogTimeAspect.class);

    @Pointcut("@annotation(logTime)")
    public void serviceStatistics(LogTime logTime) {
    }

    @Around("serviceStatistics(logTime)")
    public Object invoke(ProceedingJoinPoint joinPoint, LogTime logTime) throws Throwable {
        long l = System.currentTimeMillis();
        Exception exception = null;
        try {
            return joinPoint.proceed();
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            String className = joinPoint.getThis().toString().split("@")[0];
            String methodName = joinPoint.getSignature().getName();
            String classAndMethod = className + "." + methodName;
            StringBuffer buffer = new StringBuffer();
            buffer.append("访问地址: ").append(classAndMethod).append("; ");
            buffer.append("业务参数: ").append(joinPoint.getArgs() == null ? "" : JSON.toJSONString(joinPoint.getArgs())).append("; ");
            buffer.append("访问耗时: ").append(System.currentTimeMillis() - l).append("毫秒; ");
            if (exception != null) {
                buffer.append("异常信息: ").append(exception.getMessage()).append("; ");
                log.error(buffer.toString(), exception);
            } else {
                log.info(buffer.toString());
            }
        }

    }

}