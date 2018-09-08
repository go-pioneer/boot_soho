package com.soho.oauth2.filter;

import com.soho.oauth2.token.OAuth2LoginToken;
import com.soho.shiro.utils.SessionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by shadow on 2017/5/2.
 */
public class OAuth2AuthenticatingFilter extends AuthenticatingFilter {

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String code = httpRequest.getParameter("code");
        return new OAuth2LoginToken(code);
    }

    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        return false;
    }

    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated()) {
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            if (StringUtils.isEmpty(code) || StringUtils.isEmpty(state) || !state.equals(SessionUtils.getAttribute("state"))) {
                saveRequestAndRedirectToLogin(request, response); // 如果用户没有身份验证，且没有auth code，则重定向到服务端授权
                return false;
            }
        }
        return executeLogin(request, response); // 执行登录
    }

    // 登录成功后的回调方法 重定向到成功页面
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        issueSuccessRedirect(request, response);
        return false;
    }

    // 登录失败后的回调
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException ae, ServletRequest request,
                                     ServletResponse response) {
        Subject subject = getSubject(request, response);
        if (subject.isAuthenticated() || subject.isRemembered()) {
            try { // 如果身份验证成功了 则也重定向到成功页面
                issueSuccessRedirect(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try { // 登录失败时重定向到失败页面
                WebUtils.issueRedirect(request, response, getLoginUrl());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}