package com.soho.spring.utils;

import com.soho.spring.model.OSSConfig;
import com.soho.spring.model.RGX;
import org.springframework.util.StringUtils;

/**
 * @author shadow
 */
public class RGXUtils {

    // 正则校验
    public static boolean matches(Object object, RGX regex) {
        return StringUtils.isEmpty(object) ? false : object.toString().matches(regex.toString());
    }

    public static boolean isInteger(Object object) {
        return matches(object, RGX.INTEGER);
    }

    public static boolean isFloat(Object object) {
        return matches(object, RGX.FLOAT);
    }

    public static boolean isMobile(Object object) {
        return matches(object, RGX.MOBILE);
    }

    public static boolean isEmail(Object object) {
        return matches(object, RGX.EMAIL);
    }

    public static boolean isAccount(Object object) {
        return matches(object, RGX.ACCOUNT);
    }

    public static boolean isPassword(Object object) {
        return matches(object, RGX.PASSWORD);
    }

    public static boolean isURL(Object object) {
        return matches(object, RGX.URL);
    }

    public static boolean isImgUrl(String imgurl) {
        OSSConfig ossConfig = SpringUtils.getBean(OSSConfig.class);
        if (!StringUtils.isEmpty(imgurl) && imgurl.startsWith(ossConfig.getDomain())) {
            return true;
        }
        return false;
    }

}
