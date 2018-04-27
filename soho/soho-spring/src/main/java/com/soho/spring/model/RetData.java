package com.soho.spring.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RetData<T> implements Serializable {

    public String code;
    public String msg;
    public long ts;
    public T data;
    public Map<String, Object> http = new HashMap<>();

    public RetData() {

    }

    public RetData(String code, String msg, T data) {
        this(code, msg, System.currentTimeMillis(), data, HttpStatus.OK);
    }

    public RetData(String code, String msg, T data, HttpStatus httpStatus) {
        this(code, msg, System.currentTimeMillis(), data, httpStatus);
    }

    public RetData(String code, String msg, long ts, T data, HttpStatus httpStatus) {
        this.code = code;
        this.msg = msg;
        this.ts = ts;
        this.data = data;
        httpStatus = httpStatus == null ? HttpStatus.OK : httpStatus;
        http.put("status", httpStatus.value());
        http.put("message", httpStatus.getReasonPhrase());
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
