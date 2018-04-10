package com.soho.spring.shiro.filter;

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
    private String redirectUrl;

    public SimpleRoleAuthorizationFilter() {
    }

    public SimpleRoleAuthorizationFilter(String[] apiPrefix, String redirectUrl) {
        this.apiPrefix = apiPrefix;
        this.redirectUrl = redirectUrl;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Subject subject = getSubject(request, response);

        if (subject.getPrincipal() == null) {
            if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
                RetData<Object> retData = new RetData<>("000001", "您尚未登录或会话已超时", new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
        } else {
            if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
                RetData<Object> retData = new RetData<>("000002", "您没有足够的权限访问", new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                String unauthorizedUrl = getUnauthorizedUrl();
                if (subject.isAuthenticated()) {
                    if (StringUtils.hasText(unauthorizedUrl)) {
                        WebUtils.issueRedirect(request, response, unauthorizedUrl);
                    } else {
                        WebUtils.toHttp(response).sendError(401);
                    }
                } else {
                    if (StringUtils.hasText(redirectUrl)) {
                        WebUtils.issueRedirect(request, response, redirectUrl);
                    } else {
                        WebUtils.toHttp(response).sendError(404);
                    }
                }
            }
        }
        return false;
    }

    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws IOException {

        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            // no roles specified, so nothing to check - allow access.
            return true;
        }

        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for (String role : roles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }
        return false;
    }

}