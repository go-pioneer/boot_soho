package com.soho.shiro.filter;

import com.soho.shiro.utils.SessionUtils;
import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 用户会话校验是否踢出拦截器(支持异步请求异常提示)
 *
 * @author shadow
 */
public class SimpleKickOutSessionFilter extends AccessControlFilter {

    private String[] apiPrefix;

    public SimpleKickOutSessionFilter() {
    }

    public SimpleKickOutSessionFilter(String[] apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 获取用户对象上下文
        Subject subject = getSubject(httpRequest, httpResponse);
        // 判断用户对象是否已进行认证以及是否存在登录主体
        if (subject.isAuthenticated() && subject.getPrincipal() != null) {
            Session session = subject.getSession();
            Object online = session.getAttribute(SessionUtils.ONLINE);
            // 判断登录主体绑定的会话ID是否一致,如不一致则返回跳入失败函数处理
            if (online != null && !online.equals(1)) {
                return false;
            }
        }
        return true;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Subject subject = getSubject(httpRequest, httpResponse);
        try {
            subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
            RetData<Object> retData = new RetData<>(RetCode.SESSION_KICKOUT_STATUS, RetCode.SESSION_KICKOUT_MESSAGE, new HashMap());
            HttpUtils.responseJsonData(httpResponse, retData);
        } else {
            WebUtils.issueRedirect(request, response, getLoginUrl());
        }
        return false;
    }

}
