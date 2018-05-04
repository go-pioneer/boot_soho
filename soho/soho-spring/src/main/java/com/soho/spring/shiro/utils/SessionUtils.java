package com.soho.spring.shiro.utils;

import com.soho.spring.utils.SpringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author shadow
 */
public class SessionUtils {

    public static final String USER = "_SESSION_USER_";
    public static final String USER_ROLES = "_SESSION_USER_ROLES_";
    public static final String ONLINE = "_ONLIEN_SESSION_ID_";

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static Session getSession() {
        return getSubject().getSession();
    }

    public static <T> T getUser() {
        Object object = getSession().getAttribute(USER);
        if (object != null) {
            return (T) object;
        }
        return null;
    }

    public static void setUser(Object user, Object principal) {
        getSession().setAttribute(USER, user);
        setOnlineUserId(principal, getSession().getId().toString());
    }

    public static void setUserRoles(Object roles) {
        getSession().setAttribute(USER_ROLES, roles);
    }

    public static Set<String> getUserRoles() {
        Object object = getSession().getAttribute(USER_ROLES);
        if (object != null && object instanceof Set) {
            return (Set<String>) object;
        }
        return new HashSet<>();
    }

    public static void setOnlineUserId(Object userId, Object sessionId) {
        try {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(null);
            if (cache != null) {
                cache.put(ONLINE + userId, sessionId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getOnlineUserId(Object userId) {
        try {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(null);
            if (cache != null) {
                Object cacheValue = cache.get(ONLINE + userId);
                if (cacheValue != null) {
                    return cacheValue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static <T> T getAttribute(Object key) {
        Object object = getSession().getAttribute(key);
        if (object != null) {
            return (T) object;
        }
        return null;
    }

    public static <T> T getPrincipal() {
        Object object = getSubject().getPrincipal();
        if (object != null) {
            return (T) object;
        }
        return null;
    }

}
