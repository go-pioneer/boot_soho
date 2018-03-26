package com.soho.mybatis.interceptor.handler.imp;

import com.alibaba.fastjson.JSON;
import com.soho.mybatis.interceptor.handler.BizMonitorHandler;
import com.soho.mybatis.interceptor.handler.domain.LogObj;

public class SimpleBizMonitorHandler implements BizMonitorHandler {

	public void handle(LogObj logObj) {
		System.out.println(JSON.toJSONString(logObj));
	}

}
