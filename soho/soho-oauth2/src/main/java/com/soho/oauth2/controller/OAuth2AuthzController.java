package com.soho.oauth2.controller;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.service.OAuth2AuthzService;
import com.soho.spring.model.RetData;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.utils.HttpUtils;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shadow on 2017/5/2.
 */
public abstract class OAuth2AuthzController {

    protected abstract OAuth2AuthzService getOAuth2AuthzService();

    public Object authorize(HttpServletRequest request, HttpServletResponse response) {
        try {
            return getOAuth2AuthzService().authorize(request, response);
        } catch (BizErrorEx ex) {
            HttpUtils.responseJsonData(response, new RetData<>(ex.getErrorCode(), ex.getMessage(), ex.getErrorObject()));
            return new FastView().done();
        }
    }

    public Object access_token(HttpServletRequest request, HttpServletResponse response) {
        try {
            return getOAuth2AuthzService().access_token(request, response);
        } catch (OAuthSystemException e) {
            return OAuthError.TokenResponse.INVALID_REQUEST;
        }
    }

    public Object userinfo(HttpServletRequest request, HttpServletResponse response) {
        try {
            return getOAuth2AuthzService().userinfo(request, response);
        } catch (Exception e) {
            return new ResponseEntity(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            return getOAuth2AuthzService().logout_token(request, response);
        } catch (OAuthSystemException e) {
            return new ResponseEntity(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Object refresh(HttpServletRequest request, HttpServletResponse response) {
        try {
            return getOAuth2AuthzService().refresh_token(request, response);
        } catch (OAuthSystemException e) {
            return new ResponseEntity(
                    e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}