package com.soho.spring.shiro.initialize;

public class RuleChain {

    private String url;
    private String role;

    public RuleChain() {

    }

    public RuleChain(String url, String role) {
        this.url = url;
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
