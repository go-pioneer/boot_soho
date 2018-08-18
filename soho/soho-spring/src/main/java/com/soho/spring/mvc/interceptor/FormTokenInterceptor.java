package com.soho.spring.mvc.interceptor;

import com.soho.spring.shiro.utils.FormTokenUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * MVC拦截器,判断存在FormToken注解声明方法,必须校验
 *
 * @author shadow
 */
public class FormTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        FormTokenUtils.validFormToken(request, handler);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        FormTokenUtils.delFormToken(request, handler);
    }

}
