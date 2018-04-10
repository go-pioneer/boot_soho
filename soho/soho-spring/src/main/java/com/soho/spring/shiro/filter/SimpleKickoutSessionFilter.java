package com.soho.spring.shiro.filter;

import com.soho.spring.model.RetData;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.spring.utils.HttpUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

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
public class SimpleKickoutSessionFilter extends FormAuthenticationFilter {

    private String[] apiPrefix;
    private CacheManager cacheManager;

    public SimpleKickoutSessionFilter() {
    }

    public SimpleKickoutSessionFilter(String[] apiPrefix, CacheManager cacheManager) {
        this.apiPrefix = apiPrefix;
        this.cacheManager = cacheManager;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        Subject subject = getSubject(request, response);
        Cache cache = cacheManager.getCache(null);
        Object cacheValue = cache.get(SessionUtils.ONLINE + subject.getPrincipal());
        if (cacheValue == null || cacheValue.equals(subject.getSession().getId().toString())) {
            return true;
        }
        try {
            subject.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
            RetData<Object> retData = new RetData<>("000003", "您的会话已被踢下线", new HashMap());
            HttpUtils.responseJsonData(httpResponse, retData);
        } else {
            saveRequestAndRedirectToLogin(request, response);
        }
        return false;
    }

}
