package com.soho.spring.ex;

import java.io.Serializable;

/**
 * @author shadow
 */
public class ExModel implements Serializable {

    private String code;
    private String msg;
    private String title;
    private String callurl;
    private String data;

    public ExModel() {

    }

    public ExModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCallurl() {
        return callurl;
    }

    public void setCallurl(String callurl) {
        this.callurl = callurl;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
