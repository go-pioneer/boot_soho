package com.soho.rabbitmq.model;

import java.io.Serializable;

public class DLXMessage implements Serializable {

    public DLXMessage() {
        super();
    }

    private String exchange;

    private String queue;

    private Object content;

    private long delay;

    private int retries = 0;

    public DLXMessage(String queue, Object content, long delay) {
        super();
        this.queue = queue;
        this.content = content;
        this.delay = delay;
    }

    public DLXMessage(String exchange, String queue, Object content, long delay) {
        super();
        this.exchange = exchange;
        this.queue = queue;
        this.content = content;
        this.delay = delay;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }
}