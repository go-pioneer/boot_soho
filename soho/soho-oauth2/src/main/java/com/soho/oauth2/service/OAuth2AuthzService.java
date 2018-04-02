package com.soho.oauth2.service;

import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;

/**
 * Created by shadow on 2017/12/18.
 */
public interface OAuth2AuthzService {

    // 获取授权码
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException;

    // 获取access_token
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException;

    // 获取用户信息
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException, URISyntaxException;

    // 注销access_token
    public Object logout_token(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException;

    // 续期access_token
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws OAuthSystemException;

}
