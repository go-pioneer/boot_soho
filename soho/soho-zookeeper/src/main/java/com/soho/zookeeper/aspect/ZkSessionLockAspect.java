package com.soho.zookeeper.aspect;

import com.soho.spring.aspect.InvokeAspect;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.zookeeper.lock.ZkDistributedLock;
import org.apache.shiro.session.Session;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.locks.Lock;

/**
 * @author shadow
 */
public class ZkSessionLockAspect implements InvokeAspect {

    private String projectCode;
    private String zkUrl;

    public ZkSessionLockAspect(String projectCode, String zkUrl) {
        this.projectCode = projectCode + "_";
        this.zkUrl = zkUrl;
    }

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Lock lock = null;
        try {
            Session session = SessionUtils.getSession();
            lock = new ZkDistributedLock(zkUrl, projectCode + session.getId().toString());
            lock.lock();
            // System.out.println("===Thread " + Thread.currentThread().getId() + " running");
            return joinPoint.proceed();
        } catch (Exception e) {
            throw e;
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

}
