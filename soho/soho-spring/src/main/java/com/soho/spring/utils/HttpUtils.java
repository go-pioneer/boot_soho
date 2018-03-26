package com.soho.spring.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author shadow
 */
public class HttpUtils {

    public static final String UTF8 = "UTF-8";

    public static final String OK_STATUS = "000000";
    public static final String OK_MESSAGE = "operation successfully";

    public static final String UNKNOWN_STATUS = "999999";
    public static final String UNKNOWN_MESSAGE = "operation faliure";


    public static boolean isAjax(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            return true;
        }
        return false;
    }

}
