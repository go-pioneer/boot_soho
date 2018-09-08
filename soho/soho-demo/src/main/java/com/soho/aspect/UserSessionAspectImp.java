package com.soho.aspect;

import com.soho.session.UserSessionAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(5)
public class UserSessionAspectImp extends UserSessionAspect {

    @Around("execution(public * com.soho.*.service..*.*(..))")
    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        return super.invoke(joinPoint);
    }

}
