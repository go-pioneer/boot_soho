package com.soho.demo.domain;

import com.soho.mybatis.crud.domain.IDEntity;

@SuppressWarnings("serial")
public class OauthClientToken implements IDEntity<Long> {
    private Long id;

    private String client_id;

    private String uid;

    private String access_token;

    private String code;

    private Integer code_state;

    private Long code_expire;

    private Integer token_state;

    private Long token_expire;

    private Long access_time;

    private String refresh_token;

    private Long refresh_time;

    private Long logout_time;

    private Long ctime;

    private Long utime;

    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id == null ? null : client_id.trim();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid == null ? null : uid.trim();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token == null ? null : access_token.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getCode_state() {
        return code_state;
    }

    public void setCode_state(Integer code_state) {
        this.code_state = code_state;
    }

    public Long getCode_expire() {
        return code_expire;
    }

    public void setCode_expire(Long code_expire) {
        this.code_expire = code_expire;
    }

    public Integer getToken_state() {
        return token_state;
    }

    public void setToken_state(Integer token_state) {
        this.token_state = token_state;
    }

    public Long getToken_expire() {
        return token_expire;
    }

    public void setToken_expire(Long token_expire) {
        this.token_expire = token_expire;
    }

    public Long getAccess_time() {
        return access_time;
    }

    public void setAccess_time(Long access_time) {
        this.access_time = access_time;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token == null ? null : refresh_token.trim();
    }

    public Long getRefresh_time() {
        return refresh_time;
    }

    public void setRefresh_time(Long refresh_time) {
        this.refresh_time = refresh_time;
    }

    public Long getLogout_time() {
        return logout_time;
    }

    public void setLogout_time(Long logout_time) {
        this.logout_time = logout_time;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}