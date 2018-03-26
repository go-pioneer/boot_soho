package com.soho.mybatis.crud.domain;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shadow on 2017/6/7.
 */
public class RetMap {

    private Map<String, Object> map = new HashMap<>();

    public RetMap add(String key, Object val) {
        if (!StringUtils.isEmpty(key) && val != null)
            map.put(key, val);
        return this;
    }

    public Map<String, Object> done() {
        return map;
    }

}
