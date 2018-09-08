package com.soho.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(5)
public class UserSessionAspectImp  {

    /*@Around("execution(public * com.soho.*.service..*.*(..))")
    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.invoke(joinPoint);
    }*/

}
