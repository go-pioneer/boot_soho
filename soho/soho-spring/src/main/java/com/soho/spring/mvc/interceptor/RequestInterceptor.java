package com.soho.spring.mvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author shadow
 */
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
        Map<String, String[]> map = request.getParameterMap();
        Field lockedField = map.getClass().getDeclaredField("locked");
        lockedField.setAccessible(true);
        lockedField.setBoolean(map, false);//将lock参数设置为false了，就是可以修改了
        map.put("username", new String[]{"1.01"});
        lockedField.setBoolean(map, true);
        return true;
    }

}
