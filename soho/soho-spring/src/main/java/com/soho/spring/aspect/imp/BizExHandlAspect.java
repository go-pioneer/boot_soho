package com.soho.spring.aspect.imp;

import com.soho.spring.aspect.InvokeAspect;
import com.soho.spring.aspect.handler.BizExLog;
import com.soho.spring.aspect.handler.BizExLogHandler;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author shadow
 */
public class BizExHandlAspect implements InvokeAspect {

    private BizExLogHandler handler;

    public BizExHandlAspect(BizExLogHandler handler) {
        this.handler = handler;
    }

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Exception exception = null;
        Object retObject = null;
        try {
            retObject = joinPoint.proceed();
            return retObject;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            System.out.println(joinPoint.getThis());
            String className = joinPoint.getThis().toString().split("@")[0];
            String methodName = joinPoint.getSignature().getName();
            String logId = className + "." + methodName;
            Object[] args = joinPoint.getArgs();
            BizExLog log = new BizExLog(logId, beginTime, endTime, endTime - beginTime, args, retObject, exception);
            if (handler != null) {
                handler.handle(log);
            }
        }
    }
}
