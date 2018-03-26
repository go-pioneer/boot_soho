package com.soho.mybatis.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.soho.mybatis.interceptor.handler.BizMonitorHandler;
import com.soho.mybatis.interceptor.handler.domain.LogObj;

/**
 * Created by shadow on 2016/3/24.
 */
public class SpringBizInterceptor implements MethodInterceptor {

	private BizMonitorHandler handler;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		long beginTime = System.currentTimeMillis();
		Exception exception = null;
		Object retObject = null;
		try {
			retObject = invocation.proceed();
			return retObject;
		} catch (Exception e) {
			exception = e;
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			String logId = invocation.getThis().getClass().getName() + "." + invocation.getMethod().getName();
			LogObj logObj = new LogObj(logId, beginTime, endTime, endTime - beginTime, invocation.getArguments(), retObject, null, exception);
			if (handler != null) {
				handler.handle(logObj);
			}
		}
	}

	public BizMonitorHandler getHandler() {
		return handler;
	}

	public void setHandler(BizMonitorHandler handler) {
		this.handler = handler;
	}

}