package com.soho.spring.ex;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.utils.HttpUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author shadow
 */
@ControllerAdvice
public class GlobalExHandler {

    private String restApi;

    @ExceptionHandler(value = Exception.class)
    public Object defaultErrorHandler(HttpServletRequest request, Exception e) {
        String requestUri = request.getRequestURI();
        String queryString = StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString();
        String returnUri = "/error/errorPage";
        if (HttpUtils.isAjax(request) || (restApi != null && requestUri.startsWith(restApi))) {
            returnUri = "/error/errorJson";
        }
        String code = HttpUtils.UNKNOWN_STATUS;
        String message = HttpUtils.UNKNOWN_MESSAGE;
        try {
            if (e instanceof BizErrorEx) {
                code = ((BizErrorEx) e).getErrorCode();
                message = URLEncoder.encode(((BizErrorEx) e).getMsg(), HttpUtils.UTF8);
                requestUri = URLEncoder.encode(requestUri += queryString, HttpUtils.UTF8);
            }
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return new StringBuffer("redirect:").append(returnUri).append("?code=").append(code).append("&msg=").append(message).append("&callurl=").append(requestUri).toString();
    }

}
