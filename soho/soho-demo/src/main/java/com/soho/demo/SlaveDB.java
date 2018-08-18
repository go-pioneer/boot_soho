package com.soho.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlaveDB {

    @Value("${slave.datasource.dsName}")
    private String dsName;
    @Value("${slave.datasource.driverClassName}")
    private String driverClassName;
    @Value("${slave.datasource.url}")
    private String url;
    @Value("${slave.datasource.username}")
    private String username;
    @Value("${slave.datasource.password}")
    private String password;

    public String getDsName() {
        return dsName;
    }

    public void setDsName(String dsName) {
        this.dsName = dsName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

}
