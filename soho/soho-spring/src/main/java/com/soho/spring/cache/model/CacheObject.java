package com.soho.spring.cache.model;

import java.io.Serializable;

public class CacheObject<T> implements Serializable {

    private Object key;
    private long begin;
    private long last;
    private int expire;
    private T data;

    public CacheObject() {

    }

    public CacheObject(Object key, T data) {
        this(key, data, -1);
    }

    public CacheObject(Object key, T data, int expire) {
        this.key = key;
        this.data = data;
        this.expire = expire;
        this.begin = System.currentTimeMillis();
        this.last = begin;
    }

    public Object getKey() {
        return key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public boolean isValid() { // true有效,false失效
        if (expire == -1) {
            return true;
        }
        return (last + expire * 1000) <= System.currentTimeMillis();
    }

}
