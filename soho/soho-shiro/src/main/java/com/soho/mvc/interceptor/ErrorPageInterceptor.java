package com.soho.mvc.interceptor;

import com.soho.spring.model.DeftConfig;
import com.soho.spring.model.ErrorPageConfig;
import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * MVC拦截器,错误页面判断
 *
 * @author shadow
 */
public class ErrorPageInterceptor implements HandlerInterceptor {

    private DeftConfig deftConfig;
    private ErrorPageConfig errorPageConfig;

    public ErrorPageInterceptor() {

    }

    public ErrorPageInterceptor(DeftConfig deftConfig, ErrorPageConfig errorPageConfig) {
        this.deftConfig = deftConfig;
        this.errorPageConfig = errorPageConfig;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (HttpUtils.isRetJson(request, deftConfig.getApiPrefix())) {
            if (response.getStatus() == 403) {
                RetData retData = new RetData(RetCode.BIZ_ERROR_STATUS, "您没有足够的权限访问该资源", new HashMap<>(), HttpStatus.FORBIDDEN);
                HttpUtils.responseJsonData(response, retData);
            } else if (response.getStatus() == 404) {
                RetData retData = new RetData(RetCode.BIZ_ERROR_STATUS, "该访问资源不存在或已被移除", new HashMap<>(), HttpStatus.NOT_FOUND);
                HttpUtils.responseJsonData(response, retData);
            } else if (response.getStatus() == 500) {
                RetData retData = new RetData(RetCode.BIZ_ERROR_STATUS, "访问资源发生未知异常错误", new HashMap<>(), HttpStatus.INTERNAL_SERVER_ERROR);
                HttpUtils.responseJsonData(response, retData);
            }
        } else {
            if (response.getStatus() == 403) {
                response.sendRedirect(errorPageConfig.getError403());
            } else if (response.getStatus() == 404) {
                response.sendRedirect(errorPageConfig.getError404());
            } else if (response.getStatus() == 500) {
                response.sendRedirect(errorPageConfig.getError500());
            }
        }
    }

}
