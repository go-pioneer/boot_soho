package com.soho.spring.utils;

import com.soho.spring.model.OSSData;
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
        OSSData ossData = SpringUtils.getBean(OSSData.class);
        if (!StringUtils.isEmpty(imgurl) && imgurl.startsWith(ossData.getDomain())) {
            return true;
        }
        return false;
    }

}
