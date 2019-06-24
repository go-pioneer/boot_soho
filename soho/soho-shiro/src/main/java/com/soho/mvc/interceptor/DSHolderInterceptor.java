package com.soho.mvc.interceptor;

import com.soho.spring.datasource.DSContextHolder;
import com.soho.spring.datasource.annotation.DSHolder;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * MVC拦截器,判断存在DSHolder注解,则切换数据源
 *
 * @author shadow
 */
public class DSHolderInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String dsName = request.getParameter("dsName");
        if (!StringUtils.isEmpty(dsName)) {
            DSContextHolder.set(dsName);
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.getAnnotation(DSHolder.class) != null) {
                DSHolder annotation = method.getAnnotation(DSHolder.class);
                if (annotation != null) {
                    dsName = annotation.name();
                    if (!StringUtils.isEmpty(dsName)) {
                        DSContextHolder.set(dsName);
                    }
                }
            }
        }
        return true;
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        String dsName = request.getParameter("dsName");
        if (!StringUtils.isEmpty(dsName)) {
            DSContextHolder.clear();
            return;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.getAnnotation(DSHolder.class) != null) {
                DSHolder annotation = method.getAnnotation(DSHolder.class);
                if (annotation != null) {
                    dsName = annotation.name();
                    if (!StringUtils.isEmpty(dsName)) {
                        DSContextHolder.clear();
                        return;
                    }
                }
            }
        }
    }

}
