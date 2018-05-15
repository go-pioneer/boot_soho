package com.soho.spring.shiro.filter;

import com.soho.spring.model.RetCode;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object object) {
        Subject subject = this.getSubject(request, response);
        return subject.isAuthenticated();
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
                RetData<Object> retData = new RetData<>(RetCode.SESSION_NOTEXIST_STATUS, RetCode.SESSION_NOTEXIST_MESSAGE, new HashMap());
                HttpUtils.responseJsonData(httpResponse, retData);
            } else {
                saveRequestAndRedirectToLogin(request, response);
            }
        }
        return false;
    }

    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
//        AuthenticationToken token = createToken(request, response);
//        if (token == null) {
//            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken " +
//                    "must be created in order to execute a login attempt.";
//            throw new IllegalStateException(msg);
//        }
//        try {
//            Subject subject = getSubject(request, response);
//            Session session = subject.getSession();
//            final LinkedHashMap<Object, Object> attributes = new LinkedHashMap<Object, Object>();
//            final Collection<Object> keys = session.getAttributeKeys();
//            for (Object key : keys) {
//                final Object value = session.getAttribute(key);
//                if (value != null) {
//                    attributes.put(key, value);
//                }
//            }
//            session.stop();
//            subject.login(token);
//            session = subject.getSession(true);
//            for (final Object key : attributes.keySet()) {
//                session.setAttribute(key, attributes.get(key));
//            }
//            return onLoginSuccess(token, subject, request, response);
//        } catch (AuthenticationException e) {
//            return onLoginFailure(token, e, request, response);
//        }
        return super.executeLogin(request, response);
    }

}
