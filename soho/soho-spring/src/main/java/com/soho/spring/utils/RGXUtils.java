package com.soho.spring.utils;

import com.soho.spring.model.OSSConfig;
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

    public static boolean matchImgUrl(String imgurl) {
        OSSConfig ossConfig = SpringUtils.getBean(OSSConfig.class);
        if (!StringUtils.isEmpty(imgurl) && imgurl.startsWith(ossConfig.getDomain())) {
            return true;
        }
        return false;
    }

}
