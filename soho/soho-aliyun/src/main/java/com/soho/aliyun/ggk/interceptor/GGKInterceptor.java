package com.soho.aliyun.ggk.interceptor;

import com.soho.aliyun.ggk.utils.GGKUtils;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.GGKData;
import com.soho.spring.model.RetCode;
import com.soho.spring.mvc.annotation.FormToken;
import com.soho.spring.mvc.annotation.GGKToken;
import com.soho.spring.shiro.utils.FormTokenUtils;
import com.soho.spring.utils.HttpUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * MVC拦截器,判断存在FormToken注解声明方法,必须校验
 *
 * @author shadow
 */
public class GGKInterceptor implements HandlerInterceptor {

    private GGKData ggkData;

    public GGKInterceptor(GGKData ggkData) {
        this.ggkData = ggkData;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        GGKToken annotation = method.getAnnotation(GGKToken.class);
        if (annotation == null) {
            return true;
        }
        String queryString = StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString();
        String requestURI = request.getRequestURI() + queryString;
        for (String url : ggkData.getUrls()) {
            if (requestURI.startsWith(url)) {
                String failUrl = annotation.failUrl(); // 失败后跳转
                String ggkUrl = annotation.ggkUrl(); // GGK请求地址
                if (!GGKUtils.validate()) {
                    if (HttpUtils.isAjax(request)) {
                        throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "请先进行安全认证");
                    }
                    if (!StringUtils.isEmpty(failUrl)) {
                        response.sendRedirect(ggkUrl + "?" + failUrl);
                    } else {
                        response.sendRedirect(ggkUrl + "?" + requestURI);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        FormTokenUtils.delFormToken(request, handler);
    }

}
