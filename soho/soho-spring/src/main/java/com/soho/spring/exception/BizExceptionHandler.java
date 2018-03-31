package com.soho.spring.exception;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.ConfigData;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shadow on 2017/4/27.
 */
@Component
public class BizExceptionHandler implements HandlerExceptionResolver {

    @Autowired
    private ConfigData config;

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        String requestUri = request.getRequestURI();
        String queryString = StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString();
        Map<String, String> callmap = new HashMap<>();
        callmap.put("callurl", requestUri + queryString);
        ModelAndView view = new ModelAndView();
        RetData<Object> retData = null;
        if (ex instanceof BizErrorEx) {
            BizErrorEx errorEx = (BizErrorEx) ex;
            String msg = errorEx.getMsg();
            if (StringUtils.isEmpty(msg)) {
                msg = errorEx.getMessage();
            }
            retData = new RetData<>(errorEx.getErrorCode(), msg, callmap);
        } else {
            retData = new RetData<>(RetData.UNKNOWN_STATUS, RetData.UNKNOWN_MESSAGE, callmap);
        }
        if (HttpUtils.isRetJson(request, config.getApiPrefix())) {
            HttpUtils.responseJsonData(response, retData);
        } else {
            view.setViewName(config.getFailureUrl());
            view.addObject("retData", retData);
        }
        return view;
    }
}
