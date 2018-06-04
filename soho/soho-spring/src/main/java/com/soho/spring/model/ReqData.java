package com.soho.spring.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by shadow on 2017/9/8.
 */
public class ReqData<T, M> implements Serializable {

    private String access_token;    // 授权TOKEN
    private String client_device;   // 客户端设备 参数: android, iphone
    private String device_type;     // 客户设备具体型号
    private String app_version;     // APP版本号
    private String client_type;     // 客户端类型 参数: app, web
    private String client_uid;      // 用户UID
    private String client_pbk;      // 用户授权公钥
    private T user;                 // 用户信息,可填充ID,USER对象
    private Map<String, Object> pojo;                // 表单提交对象数据
    private Integer pageNo = 1;
    private Integer pageSize = 50;

    public ReqData() {

    }

    public ReqData(T user) {
        this.user = user;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getClient_device() {
        return client_device;
    }

    public void setClient_device(String client_device) {
        this.client_device = client_device;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getClient_uid() {
        return client_uid;
    }

    public void setClient_uid(String client_uid) {
        this.client_uid = client_uid;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }

    public Map<String, Object> getPojo() {
        return pojo;
    }

    public void setPojo(Map<String, Object> pojo) {
        this.pojo = pojo;
    }

    public M getModel() {
        if (pojo != null && !pojo.isEmpty()) {
            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            return JSON.parseObject(JSON.toJSONString(pojo), type);
        }
        return null;
    }

    public String getClient_pbk() {
        return client_pbk;
    }

    public void setClient_pbk(String client_pbk) {
        this.client_pbk = client_pbk;
    }

}
