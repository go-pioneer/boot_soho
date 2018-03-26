package com.soho.mybatis.interceptor;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MybatisInterceptor implements Interceptor {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Properties properties;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return invocation.proceed();
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
		afterSetProperties();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	
	protected void afterSetProperties() {
	}

}
