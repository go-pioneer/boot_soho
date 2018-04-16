package com.soho.spring.utils;

import com.soho.spring.model.RGX;
import org.springframework.util.StringUtils;

/**
 * @author shadow
 */
public class RGXUtils {

    // 正则校验
    public static boolean matches(String object, RGX regex) {
        if (StringUtils.isEmpty(object)) {
            return false;
        }
        return object.matches(regex.toString());
    }

    public static void main(String[] args) {
        String s = "0.0";
        long l1 = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            long l2 = System.currentTimeMillis();
            if (s.length() > 0) {
                boolean b = matches(s, RGX.FLOAT);
            }
            System.out.println(((System.currentTimeMillis() - l2)) + "毫秒");
        }
        System.out.println("最终合计耗时:" + ((System.currentTimeMillis() - l1)) + "毫秒");
    }

}
