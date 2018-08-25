package com.soho.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "demo_user")
public class UserEntity implements Serializable {

    @Id
    private Long id;
    private String username;
    private String password;

    private UserInfo child;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserInfo getChild() {
        return child;
    }

    public void setChild(UserInfo child) {
        this.child = child;
    }
}