package com.soho.spring.shiro.session;

import com.soho.spring.utils.SpringUtils;
import com.soho.spring.utils.WCCUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;
import java.util.List;

/**
 * @author shadow
 */
public class ShiroSessionDAO extends EnterpriseCacheSessionDAO {

    private List<String> anonUrls;

    public ShiroSessionDAO() {

    }

    public ShiroSessionDAO(List<String> anonUrls) {
        this.anonUrls = anonUrls;
    }

    protected Session doReadSession(Serializable sessionId) {
        return matchAnonRequest() ? null : super.doReadSession(sessionId);
    }

    protected void doUpdate(Session session) {
        if (matchAnonRequest()) {
            return;
        }
        super.doUpdate(session);
    }

    private boolean matchAnonRequest() {
        if (anonUrls != null && !anonUrls.isEmpty()) {
            String reqeust = SpringUtils.getRequest().getRequestURI();
            for (String anonUrl : anonUrls) {
                if (anonUrl.equals(reqeust) || WCCUtils.test(anonUrl, reqeust)) {
                    return true;
                }
            }
        }
        return false;
    }

}
