package com.soho.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "demo_user")
public class UserInfo implements Serializable {

    private String username;
    private String password;

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