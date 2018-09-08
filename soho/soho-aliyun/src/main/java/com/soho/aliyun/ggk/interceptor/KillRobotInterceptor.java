package com.soho.aliyun.ggk.interceptor;

import com.soho.mvc.annotation.KillRobot;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.shiro.utils.KillRobotUtils;
import com.soho.spring.model.RetCode;
import com.soho.spring.utils.HttpUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.net.URLEncoder;

/**
 * MVC拦截器,防机器人请求访问校验
 *
 * @author shadow
 */
public class KillRobotInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        KillRobot annotation = method.getAnnotation(KillRobot.class);
        if (annotation == null) {
            return true;
        }
        String queryString = StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString();
        String requestURI = request.getRequestURI() + queryString;
        String goback = annotation.goback(); // 返回上一个请求
        String valid = annotation.valid(); // 认证地址
        if (!KillRobotUtils.validate()) {
            if (HttpUtils.isJsonResponse(handler) || HttpUtils.isAjax(request)) {
                throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "请先进行安全认证");
            }
            if (!StringUtils.isEmpty(goback)) {
                response.sendRedirect(valid + "?callurl=" + URLEncoder.encode(goback, "UTF-8"));
            } else {
                response.sendRedirect(valid + "?callurl=" + URLEncoder.encode(requestURI, "UTF-8"));
            }
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        KillRobot annotation = method.getAnnotation(KillRobot.class);
        if (annotation == null) {
            return;
        }
        Object object = request.getAttribute(KillRobotUtils.KEEP_ROBOT_VALID);
        if (object == null || !KillRobotUtils.KEEP_ROBOT_VALID.equals(object)) { // 正常流程请求,需要把认证标识清除
            if (annotation.reset()) {
                KillRobotUtils.release();
            }
        }
        if (object != null) {
            request.removeAttribute(KillRobotUtils.KEEP_ROBOT_VALID);
        }
    }

}
