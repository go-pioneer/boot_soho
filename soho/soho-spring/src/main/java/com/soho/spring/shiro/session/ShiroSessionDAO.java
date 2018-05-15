package com.soho.spring.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

public class ShiroSessionDAO extends EnterpriseCacheSessionDAO {

    public ShiroSessionDAO() {

    }

    protected Session doReadSession(Serializable sessionId) {
        return super.doReadSession(sessionId);
    }

    protected void doUpdate(Session session) {
        super.doUpdate(session);
    }

    protected void doDelete(Session session) {
        super.doDelete(session);
    }

}
