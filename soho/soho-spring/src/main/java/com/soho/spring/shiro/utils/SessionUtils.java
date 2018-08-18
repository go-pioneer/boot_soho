package com.soho.spring.shiro.utils;

import com.soho.mybatis.crud.domain.IDEntity;
import com.soho.spring.utils.SpringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;
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

    public static String getSessionId() {
        return getSubject().getSession().getId().toString();
    }

    public static <T> T getUser() {
        Object object = getSession().getAttribute(USER);
        if (object != null) {
            return (T) object;
        }
        return null;
    }

    public static Long getUserId() {
        Object object = getUser();
        if (object != null && object instanceof IDEntity) {
            IDEntity<Long> idEntity = (IDEntity<Long>) object;
            return idEntity.getId();
        }
        return null;
    }

    public static void doCreateNewSession(Object user, Object principal, boolean only) {
        if (only) {
            kickoutUser(principal);
        }
        getSecurityManager().logout(getSubject()); // 注销旧主体,自动创建新主体
        buidSessionUser(user, principal);
    }

    public static void buidSessionUser(Object user, Object principal) {
        Session session = getSession();
        session.setAttribute(USER, user);
        session.setAttribute(ONLINE, 1);
        buidOnlineSessionId(principal, session.getId());
    }

    public static void kickoutUser(final Object principal) {
        SessionManager manager = SpringUtils.getBean(SessionManager.class);
        final Object sessionId = getOnlineSessionId(principal);
        if (sessionId != null) {
            try {
                Session session = manager.getSession(new SessionKey() {
                    @Override
                    public Serializable getSessionId() {
                        return sessionId.toString();
                    }
                });
                if (session != null) {
                    session.setAttribute(ONLINE, 2);
                }
            } catch (UnknownSessionException e) {
                // e.printStackTrace();
            }
        }
    }

    public static void kickoutUser() {
        kickoutUser(getUserId());
    }

    public static SecurityManager getSecurityManager() {
        return SecurityUtils.getSecurityManager();
    }

    public static void buildUserRoles(Object roles) {
        getSession().setAttribute(USER_ROLES, roles);
    }

    public static Set<String> getUserRoles() {
        Object object = getSession().getAttribute(USER_ROLES);
        if (object != null && object instanceof Set) {
            return (Set<String>) object;
        }
        return new HashSet<>();
    }

    public static void buidOnlineSessionId(Object principal, Object sessionId) {
        try {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(null);
            if (cache != null) {
                cache.put(ONLINE + principal, sessionId.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getOnlineSessionId(Object principal) {
        try {
            Cache cache = SpringUtils.getBean(CacheManager.class).getCache(null);
            if (cache != null) {
                Object cacheValue = cache.get(ONLINE + principal);
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

    public static void removeAttribute(Object key) {
        getSession().removeAttribute(key);
    }

    public static <T> T getPrincipal() {
        Object object = getSubject().getPrincipal();
        if (object != null) {
            return (T) object;
        }
        return null;
    }

    public static void logout() {
        try {
            Session session = getSession();
            session.removeAttribute(USER_ROLES);
            session.removeAttribute(USER);
            session.removeAttribute(ONLINE);
            getSubject().logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T login(AuthenticationToken token, boolean user) {
        getSubject().login(token);
        if (user) {
            return getUser();
        }
        return null;
    }

}
