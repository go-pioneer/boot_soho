package com.soho.oauth2.model;

/**
 * Created by shadow on 2017/5/2.
 */
public class OAuth2Client {

    private String client_id;
    private String client_secret;
    private String client_type;
    private String redirect_uri;
    private String access_token_uri;
    private String authorize_code_uri;
    private String logout_token_uri;
    private String userinfo_uri;
    private String username;
    private String password;
    private String response_type = "code";
    private String grant_type = "authorization_code";
    private String state;
    private String domain_uri;
    private Integer usestate; // 1.正常 2.冻结 3.删除

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getClient_type() {
        return client_type;
    }

    public void setClient_type(String client_type) {
        this.client_type = client_type;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public void setRedirect_uri(String redirect_uri) {
        this.redirect_uri = redirect_uri;
    }

    public String getAccess_token_uri() {
        return access_token_uri;
    }

    public void setAccess_token_uri(String access_token_uri) {
        this.access_token_uri = access_token_uri;
    }

    public String getAuthorize_code_uri() {
        return authorize_code_uri;
    }

    public void setAuthorize_code_uri(String authorize_code_uri) {
        this.authorize_code_uri = authorize_code_uri;
    }

    public String getLogout_token_uri() {
        return logout_token_uri;
    }

    public void setLogout_token_uri(String logout_token_uri) {
        this.logout_token_uri = logout_token_uri;
    }

    public String getUserinfo_uri() {
        return userinfo_uri;
    }

    public void setUserinfo_uri(String userinfo_uri) {
        this.userinfo_uri = userinfo_uri;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResponse_type() {
        return response_type;
    }

    public void setResponse_type(String response_type) {
        this.response_type = response_type;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDomain_uri() {
        return domain_uri;
    }

    public void setDomain_uri(String domain_uri) {
        this.domain_uri = domain_uri;
    }

    public Integer getUsestate() {
        return usestate;
    }

    public void setUsestate(Integer usestate) {
        this.usestate = usestate;
    }

}
