package com.soho.spring.model;

import java.io.Serializable;

public class RetData<T> implements Serializable {

    public static final String OK_STATUS = "000000";
    public static final String OK_MESSAGE = "operation successfully";

    public static final String UNKNOWN_STATUS = "999999";
    public static final String UNKNOWN_MESSAGE = "operation faliure";

    public String code;
    public String msg;
    public long ts;
    public T data;

    public RetData() {

    }

    public RetData(String code, String msg, T data) {
        this(code, msg, System.currentTimeMillis(), data);
    }

    public RetData(String code, String msg, long ts, T data) {
        this.code = code;
        this.msg = msg;
        this.ts = ts;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
