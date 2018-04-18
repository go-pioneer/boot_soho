package com.soho.spring.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class FastMap {

    private Map<String, Object> map;

    public FastMap() {
        map = new HashMap<>();
    }

    public FastMap add(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public FastMap addAll(Map<String, Object> allmap) {
        map.putAll(allmap);
        return this;
    }

    public Map<String, Object> done() {
        return map;
    }

}
