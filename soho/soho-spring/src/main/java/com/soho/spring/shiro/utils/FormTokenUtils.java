package com.soho.spring.shiro.utils;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.RetCode;
import com.soho.spring.mvc.annotation.FormToken;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author shadow
 */
public class FormTokenUtils {

    public static final String FORM_TOKEN = "_SECURITY_FORM_TOKEN_";

    public static String addFormToken() {
        String form_token = UUID.randomUUID().toString();
        SessionUtils.setAttribute(FORM_TOKEN, form_token);
        return "<input type=\"hidden\" name=\"" + FORM_TOKEN + "\" value=\"" + form_token + "\"/>";
    }

    public static void validFormToken(HttpServletRequest request, Object handler) throws BizErrorEx {
        if (!isFormTokenRequest(handler)) {
            return;
        }
        String form_token = request.getParameter(FormTokenUtils.FORM_TOKEN);
        if (StringUtils.isEmpty(form_token) || form_token.length() > 100) {
            throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "数据安全校验参数异常");
        }
        if (!form_token.equals(SessionUtils.getAttribute(FORM_TOKEN))) {
            throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "数据安全校验失败,请重新刷新页面");
        }
    }

    public static void delFormToken(HttpServletRequest request, Object handler) {
        if (!isFormTokenRequest(handler)) {
            return;
        }
        Object object = request.getAttribute(FORM_TOKEN);
        if (object == null || !FORM_TOKEN.equals(object)) {
            SessionUtils.removeAttribute(FORM_TOKEN);
        }
        request.removeAttribute(FORM_TOKEN);
    }

    public static void holdFormToken(HttpServletRequest request, Object handler) {
        if (!isFormTokenRequest(handler)) {
            return;
        }
        request.setAttribute(FormTokenUtils.FORM_TOKEN, FormTokenUtils.FORM_TOKEN);
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
