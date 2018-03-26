package com.soho.mybatis.interceptor.handler.imp;

import com.alibaba.fastjson.JSON;
import com.soho.mybatis.interceptor.handler.SqlMonitorHandler;
import com.soho.mybatis.interceptor.handler.domain.LogObj;

public class SimpleSqlMonitorHandler implements SqlMonitorHandler {

	public void handle(LogObj logObj) {
		System.out.println(JSON.toJSONString(logObj));
	}

}
