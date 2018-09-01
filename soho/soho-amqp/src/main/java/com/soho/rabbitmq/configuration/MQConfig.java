package com.soho.rabbitmq.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

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
    @Value("${mq.rabbit.exchange}")
    private String exchange; // 发送信道交换机名称
    @Value("${mq.rabbit.sendQueues}")
    private String sendQueues; // 发送队列名称列表
    @Value("${mq.rabbit.listenerQueues}")
    private String listenerQueues; // 监听队列名称列表
    @Value("${mq.rabbit.concurrentConsumers}")
    private Integer concurrentConsumers; // 消费者并发数量
    @Value("${mq.rabbit.maxConcurrentConsumers}")
    private Integer maxConcurrentConsumers; // 最大消费者并发数量

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

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getSendQueues() {
        return sendQueues;
    }

    public void setSendQueues(String sendQueues) {
        this.sendQueues = sendQueues;
    }

    public String getListenerQueues() {
        return listenerQueues;
    }

    public void setListenerQueues(String listenerQueues) {
        this.listenerQueues = listenerQueues;
    }

    public Integer getConcurrentConsumers() {
        return concurrentConsumers;
    }

    public void setConcurrentConsumers(Integer concurrentConsumers) {
        this.concurrentConsumers = concurrentConsumers;
    }

    public Integer getMaxConcurrentConsumers() {
        return maxConcurrentConsumers;
    }

    public void setMaxConcurrentConsumers(Integer maxConcurrentConsumers) {
        this.maxConcurrentConsumers = maxConcurrentConsumers;
    }

}
