package com.soho.spring.aspect.imp;

import com.soho.spring.aspect.InvokeAspect;
import com.soho.spring.model.ReqData;
import com.soho.spring.shiro.utils.SessionUtils;
import org.apache.shiro.session.Session;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author shadow
 */
public class UserSessionAspect implements InvokeAspect {

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        if (objects != null && objects.length > 0) {
            Object[] newobjects = new Object[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] instanceof ReqData<?, ?>) {
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
        return joinPoint.proceed();
    }
}
