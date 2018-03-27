package com.soho.aspect;

import com.soho.spring.model.ReqData;
import com.soho.spring.shiro.utils.SessionUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(5)
public class UseSessionAspect {

    @Pointcut("execution(public * com.soho.*.service..*.*(..))")
    public void pointcutMethod() {
    }

    @Before("pointcutMethod()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] objects = joinPoint.getArgs();
        if (objects != null && objects.length > 0) {
            Object[] newobjects = new Object[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof ReqData<?>) {
                    ReqData reqData = (ReqData) objects[i];
                    Session session = SessionUtils.getSession();
                    if (session != null) {
                        Object user = session.getAttribute(SessionUtils.USER);
                        if (user != null) {
                            reqData.setUser(user);
                            newobjects[i] = reqData;
                        } else {
                            newobjects[i] = objects[i];
                        }
                    } else {
                        newobjects[i] = objects[i];
                    }
                } else {
                    newobjects[i] = objects[i];
                }
            }
        }

    }


    // @AfterReturning("pointcutMethod()")
    public void doAfter(JoinPoint joinPoint) {

    }

}
