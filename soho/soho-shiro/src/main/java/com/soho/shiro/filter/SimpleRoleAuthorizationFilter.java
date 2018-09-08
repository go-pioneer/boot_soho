package com.soho.shiro.filter;

import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * 1.自定义角色鉴权过滤器(满足其中一个角色则认证通过) 2.扩展异步请求认证提示功能;
 *
 * @author shadow
 */
public class SimpleRoleAuthorizationFilter extends AuthorizationFilter {

    private String[] apiPrefix;
    private String unauthorizedUrl;

    public SimpleRoleAuthorizationFilter() {
    }

    public SimpleRoleAuthorizationFilter(String[] apiPrefix, String unauthorizedUrl) {
        this.apiPrefix = apiPrefix;
        this.unauthorizedUrl = unauthorizedUrl;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 获取用户对象上下文
        Subject subject = getSubject(httpRequest, httpResponse);
        // 没有登录状态,返回会话未登录或已超时状态
        if (!subject.isAuthenticated() || subject.getPrincipal() == null) {
            if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
                RetData<Object> retData = new RetData<>(RetCode.SESSION_NOTEXIST_STATUS, RetCode.SESSION_NOTEXIST_MESSAGE, new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                saveRequestAndRedirectToLogin(httpRequest, httpResponse);
            }
        } else {
            // 已有登录状态,则返回没有权限访问状态
            if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
                RetData<Object> retData = new RetData<>(RetCode.UNAUTHORIZED_STATUS, RetCode.UNAUTHORIZED_MESSAGE, new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                // 读取无权限回调地址
                if (subject.isAuthenticated()) {
                    if (StringUtils.hasText(getUnauthorizedUrl())) {
                        WebUtils.issueRedirect(httpRequest, httpResponse, getUnauthorizedUrl());
                    } else {
                        WebUtils.toHttp(httpResponse).sendError(401);
                    }
                } else {
                    if (StringUtils.hasText(unauthorizedUrl)) {
                        WebUtils.issueRedirect(httpRequest, httpResponse, unauthorizedUrl);
                    } else {
                        WebUtils.toHttp(httpResponse).sendError(404);
                    }
                }
            }
        }
        return false;
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;
        // 系统角色集合为空则不进行校验
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        // 系统存在校验,则匹对用户对象所拥有角色集合
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}