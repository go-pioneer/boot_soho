package com.soho.spring.mvc.handler;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.ConfigData;
import com.soho.spring.model.OSSData;
import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
@Component
public class BizExceptionHandler implements HandlerExceptionResolver {

    @Autowired
    private ConfigData configData;
    @Autowired
    private OSSData ossData;

    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        String requestUri = request.getRequestURI();
        String queryString = StringUtils.isEmpty(request.getQueryString()) ? "" : "?" + request.getQueryString();
        Map<String, String> callmap = new HashMap<>();
        callmap.put("callurl", requestUri + queryString);
        RetData<Object> retData = null;
        if (ex instanceof BizErrorEx) {
            BizErrorEx errorEx = (BizErrorEx) ex;
            String msg = errorEx.getMsg();
            if (StringUtils.isEmpty(msg)) {
                msg = errorEx.getMessage();
            }
            Object errorObject = ((BizErrorEx) ex).getErrorObject();
            HttpStatus httpStatus = ((BizErrorEx) ex).getHttpStatus();
            if (errorObject != null) {
                retData = new RetData<>(errorEx.getErrorCode(), msg, errorObject, httpStatus);
            } else {
                retData = new RetData<>(errorEx.getErrorCode(), msg, callmap, httpStatus);
            }
        } else if (ex instanceof MaxUploadSizeExceededException) {
            retData = new RetData<>(RetCode.UPLOAD_ERROR_STATUS, "上传失败,文件大小超出范围;单文件【" + ossData.getMaxFileSize() + "】,多文件【" + ossData.getMaxRequestSize() + "】", callmap);
        } else {
            retData = new RetData<>(RetCode.UNKNOWN_STATUS, RetCode.UNKNOWN_MESSAGE, callmap);
        }
        if (HttpUtils.isRetJson(request, configData.getApiPrefix())) {
            HttpUtils.responseJsonData(response, retData);
            return new FastView().done();
        } else {
            return new FastView(configData.getFailureUrl()).add("retData", retData).done();
        }
    }
}
