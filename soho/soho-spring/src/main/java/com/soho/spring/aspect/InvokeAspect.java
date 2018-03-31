package com.soho.spring.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by Administrator on 2018/3/31.
 */
public interface InvokeAspect {

    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable;

}
