package com.soho.spring.shiro.filter;

import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * 登录过滤器扩展(支持异步请求异常提示)
 *
 * @author shadow
 */
public class SimpleFormAuthenticationFilter extends FormAuthenticationFilter {

    private String[] apiPrefix;

    public SimpleFormAuthenticationFilter() {
    }

    public SimpleFormAuthenticationFilter(String[] apiPrefix) {
        this.apiPrefix = apiPrefix;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isLoginRequest(request, response)) {
            if (isLoginSubmission(request, response)) {
                return executeLogin(request, response);
            }
            return true;
        } else {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            if (HttpUtils.isRetJson(httpRequest, apiPrefix)) {
                RetData<Object> retData = new RetData<>("000001", "您尚未登录或会话已超时", new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
        }
        return false;
    }

}
