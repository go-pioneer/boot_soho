package com.soho.spring.aspect.handler;

import java.io.Serializable;

public class BizExLog implements Serializable {

    private String logId; // 类名+方法名
    private long beginTime; // 请求时间
    private long endTime; // 结束时间
    private long runTime; // 运行时间
    private Object reqObject; // 请求参数
    private Object retObject; // 返回参数
    private Exception exception; // 异常信息

    public BizExLog() {

    }

    public BizExLog(String logId, long beginTime, long endTime, long runTime, Object reqObject, Object retObject, Exception exception) {
        this.logId = logId;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.runTime = runTime;
        this.reqObject = reqObject;
        this.retObject = retObject;
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


}
