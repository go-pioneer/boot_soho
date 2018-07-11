package com.soho.spring.utils;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/7/5.
 */
public class SignUtils {

    public static String getSign(TreeMap<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue() != null ? entry.getValue().toString() : "";
            if (!StringUtils.isEmpty(key) && !"sign".equals(key)) {
                buffer.append("&").append(key).append("=").append(value);
            }
        }
        return MD5Utils.encrypt(buffer.substring(1));
    }

    public static boolean validSign(TreeMap<String, Object> map, String validSign) {
        String buildSign = getSign(map);
        if (!StringUtils.isEmpty(validSign) && validSign.equals(buildSign)) {
            return true;
        }
        return false;
    }

}
