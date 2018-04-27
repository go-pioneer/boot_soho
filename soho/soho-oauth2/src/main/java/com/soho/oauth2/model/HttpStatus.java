package com.soho.oauth2.model;

import java.io.Serializable;

/**
 * Created by shadow on 2018/4/27.
 */
public class HttpStatus implements Serializable {

    private Integer status;
    private String error;

    public HttpStatus() {
        this(200, "");
    }

    public HttpStatus(Integer status, String error) {
        this.status = status;
        this.error = error;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
