package com.soho.spring.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

    public static boolean isRetJson(HttpServletRequest request, String[] apiPrefix) {
        String requestUri = request.getRequestURI();
        if (HttpUtils.isAjax(request)) {
            return true;
        }
        if (apiPrefix != null && apiPrefix.length > 0) {
            for (String prefix : apiPrefix) {
                if (requestUri.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void responseJsonData(HttpServletResponse response, Object object) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(object));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
