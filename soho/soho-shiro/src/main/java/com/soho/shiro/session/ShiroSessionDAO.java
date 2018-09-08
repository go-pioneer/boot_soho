package com.soho.shiro.session;

import com.soho.spring.utils.SpringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author shadow
 */
public class ShiroSessionDAO extends EnterpriseCacheSessionDAO {

    private String staticPrefix;

    public ShiroSessionDAO(String staticPrefix) {
        this.staticPrefix = staticPrefix;
    }

    protected Session doReadSession(Serializable sessionId) {
        return validStaticPrefix() ? null : super.doReadSession(sessionId);
    }

    protected void doUpdate(Session session) {
        if (validStaticPrefix()) {
            return;
        }
        super.doUpdate(session);
    }

    protected void doDelete(Session session) {
        if (validStaticPrefix()) {
            return;
        }
        super.doDelete(session);
    }

    private boolean validStaticPrefix() {
        String request = SpringUtils.getRequest().getRequestURI();
        if (!StringUtils.isEmpty(staticPrefix) && !StringUtils.isEmpty(request) && request.startsWith(staticPrefix)) {
            return true;
        }
        return false;
    }

}
