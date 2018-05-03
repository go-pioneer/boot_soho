package com.soho.spring.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class FastMap<T> {

    private Map<String, T> map;

    public FastMap() {
        map = new HashMap<>();
    }

    public FastMap add(String key, T value) {
        map.put(key, value);
        return this;
    }

    public FastMap addAll(Map<String, T> allmap) {
        map.putAll(allmap);
        return this;
    }

    public Map<String, T> done() {
        return map;
    }

}
