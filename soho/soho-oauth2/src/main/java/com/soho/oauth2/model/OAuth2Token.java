package com.soho.oauth2.model;

/**
 * Created by shadow on 2017/5/2.
 */
public class OAuth2Token {

    private String client_id;
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
    private String redirect_uri;
    private String uid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        this.refresh_token = refresh_token;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

}
