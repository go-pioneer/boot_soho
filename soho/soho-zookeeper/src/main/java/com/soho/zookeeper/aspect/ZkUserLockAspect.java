package com.soho.zookeeper.aspect;

import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.spring.aspect.InvokeAspect;
import com.soho.zookeeper.lock.ZkDistributedLock;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.concurrent.locks.Lock;

/**
 * @author shadow
 */
public class ZkUserLockAspect implements InvokeAspect {

    private String projectCode;
    private String zkUrl;

    public ZkUserLockAspect(String projectCode, String zkUrl) {
        this.projectCode = projectCode + "_";
        this.zkUrl = zkUrl;
    }

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        Lock lock = null;
        try {
            for (Object object : joinPoint.getArgs()) {
                if (object instanceof IDEntity) {
                    IDEntity<?> entity = (IDEntity<?>) object;
                    lock = new ZkDistributedLock(zkUrl, projectCode + entity.getId());
                    lock.lock();
                }
            }
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
