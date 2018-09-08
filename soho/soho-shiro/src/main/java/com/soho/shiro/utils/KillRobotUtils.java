package com.soho.shiro.utils;

import com.soho.mvc.annotation.KillRobot;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by shadow on 2018/5/24.
 */
public class KillRobotUtils {

    public static final String KEEP_ROBOT_VALID = "KEEP_ROBOT_VALID";
    private static final String ROBOT_VALID_OK = "ROBOT_VALID_OK";

    public static boolean validate() {
        Object object = SessionUtils.getAttribute(ROBOT_VALID_OK);
        if (!StringUtils.isEmpty(object)) {
            return true;
        }
        return false;
    }

    public static void release() {
        if (SessionUtils.getAttribute(ROBOT_VALID_OK) != null) {
            SessionUtils.removeAttribute(ROBOT_VALID_OK);
        }
    }

    public static void success() {
        SessionUtils.setAttribute(ROBOT_VALID_OK, 1);
    }

    public static void keepKillRobot(HttpServletRequest request, Object handler) {
        if (isKillRobotRequest(handler)) {
            request.setAttribute(KEEP_ROBOT_VALID, KEEP_ROBOT_VALID);
        }
    }

    public static boolean isKillRobotRequest(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (method.getAnnotation(KillRobot.class) != null) {
                return true;
            }
        }
        return false;
    }

}
