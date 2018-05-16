package com.soho.spring.shiro.utils;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.RGX;
import com.soho.spring.model.RetCode;
import com.soho.spring.mvc.annotation.FormToken;
import com.soho.spring.utils.NumUtils;
import com.soho.spring.utils.RGXUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author shadow
 */
public class FormTokenUtils {

    public static final String SECURITY_FORM_SN = "_SECURITY_FORM_SN_";
    public static final String SECURITY_FORM_TOKEN = "_SECURITY_FORM_TOKEN_";

    public static String addFormToken() {
        String form_sn = NumUtils.getIntSN(6);
        String form_token = NumUtils.getStrSN();
        SessionUtils.setAttribute(form_sn, form_token);
        StringBuffer buffer = new StringBuffer();
        buffer.append("<input type=\"hidden\" name=\"").append(SECURITY_FORM_SN).append("\" value=\"").append(form_sn).append("\"/>");
        buffer.append("<input type=\"hidden\" name=\"").append(SECURITY_FORM_TOKEN).append("\" value=\"").append(form_token).append("\"/>");
        return buffer.toString();
    }

    public static void validFormToken(HttpServletRequest request, Object handler) throws BizErrorEx {
        if (isFormTokenRequest(handler)) {
            String form_sn = request.getParameter(SECURITY_FORM_SN);
            String form_token = request.getParameter(SECURITY_FORM_TOKEN);
            if (StringUtils.isEmpty(form_sn) || !RGXUtils.matches(form_sn, RGX.INTEGER)) {
                throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "序列编号安全校验参数异常");
            }
            if (StringUtils.isEmpty(form_token) || form_token.length() > 100) {
                throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "序列内容安全校验参数异常");
            }
            if (!form_token.equals(SessionUtils.getAttribute(form_sn))) {
                throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "表单安全校验失败,请重新刷新页面");
            }
        }
    }

    public static void delFormToken(HttpServletRequest request, Object handler) {
        if (isFormTokenRequest(handler)) {
            Object object = request.getAttribute(SECURITY_FORM_SN);
            if (object == null || !SECURITY_FORM_SN.equals(object)) { // 正常流程请求,需要把Token清除
                String form_sn = request.getParameter(SECURITY_FORM_SN);
                if (!StringUtils.isEmpty(form_sn)) {
                    SessionUtils.removeAttribute(form_sn);
                }
            }
            if (object != null) {
                request.removeAttribute(SECURITY_FORM_SN);
            }
        }
    }

    public static void holdFormToken(HttpServletRequest request, Object handler) {
        if (isFormTokenRequest(handler)) {
            request.setAttribute(SECURITY_FORM_SN, SECURITY_FORM_SN);
        }
    }

    public static boolean isFormTokenRequest(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.getAnnotation(FormToken.class) != null) {
                return true;
            }
        }
        return false;
    }

}
