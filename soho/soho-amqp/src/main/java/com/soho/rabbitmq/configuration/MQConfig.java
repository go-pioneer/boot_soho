package com.soho.rabbitmq.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shadow
 */
@Component
public class MQConfig {

    @Value("${mq.rabbit.host}")
    private String host;
    @Value("${mq.rabbit.port}")
    private Integer port;
    @Value("${mq.rabbit.virtualHost}")
    private String virtualHost;
    @Value("${mq.rabbit.username}")
    private String username;
    @Value("${mq.rabbit.password}")
    private String password;
    @Value("${mq.rabbit.publisherConfirms}")
    private Boolean publisherConfirms;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
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

    public Boolean getPublisherConfirms() {
        return publisherConfirms;
    }

    public void setPublisherConfirms(Boolean publisherConfirms) {
        this.publisherConfirms = publisherConfirms;
    }

}
