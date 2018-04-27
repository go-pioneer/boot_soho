package com.soho.oauth2;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.controller.OAuth2AuthzController;
import com.soho.oauth2.service.OAuth2AuthzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shadow on 2017/5/1.
 */
@Controller
@RequestMapping
public class OAuth2Controller extends OAuth2AuthzController {

    @Autowired
    private OAuth2AuthzService oAuth2AuthzService;

    @Override
    protected OAuth2AuthzService getOAuth2AuthzService() {
        return oAuth2AuthzService;
    }

    @RequestMapping(value = {"/oauth2.0/v1.0/authorize"})
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return super.authorize(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/oauth2.0/v1.0/access_token"}, method = {RequestMethod.POST})
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return super.access_token(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/oauth2.0/v1.0/userinfo"}, method = {RequestMethod.POST})
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return super.userinfo(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/oauth2.0/v1.0/logout"}, method = {RequestMethod.POST})
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return super.logout(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/oauth2.0/v1.0/refresh_token"}, method = {RequestMethod.POST})
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return super.refresh_token(request, response);
    }

}
