package com.soho.mybatis.interceptor.handler;

import com.soho.mybatis.interceptor.handler.domain.LogObj;

public interface SqlMonitorHandler {

	public void handle(LogObj logObj);

}
