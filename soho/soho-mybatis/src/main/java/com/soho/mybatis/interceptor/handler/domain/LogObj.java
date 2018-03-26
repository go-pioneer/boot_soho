package com.soho.mybatis.interceptor.handler.domain;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogObj implements Serializable {

	private String logId;
	private long beginTime;
	private long endTime;
	private long runTime;
	private Object reqObject;
	private Object retObject;
	private Object logObject;
	private Exception exception;

	public LogObj() {

	}

	public LogObj(String logId, long beginTime, long endTime, long runTime, Object reqObject, Object retObject, Object logObject, Exception exception) {
		this.logId = logId;
		this.beginTime = beginTime;
		this.endTime = endTime;
		this.runTime = runTime;
		this.reqObject = reqObject;
		this.retObject = retObject;
		this.logObject = logObject;
		this.exception = exception;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getRunTime() {
		return runTime;
	}

	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}

	public Object getReqObject() {
		return reqObject;
	}

	public void setReqObject(Object reqObject) {
		this.reqObject = reqObject;
	}

	public Object getRetObject() {
		return retObject;
	}

	public void setRetObject(Object retObject) {
		this.retObject = retObject;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Object getLogObject() {
		return logObject;
	}

	public void setLogObject(Object logObject) {
		this.logObject = logObject;
	}

}
