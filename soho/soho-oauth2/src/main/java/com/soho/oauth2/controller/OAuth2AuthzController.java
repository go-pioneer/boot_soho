package com.soho.oauth2.controller;

import com.soho.mvc.annotation.FormToken;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.oauth2.service.OAuth2AuthzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shadow on 2017/5/2.
 */

@Controller
@RequestMapping("/oauth2.0/v1.0")
public class OAuth2AuthzController {

    @Autowired(required = false)
    private OAuth2AuthzService oAuth2AuthzService;

    @FormToken
    @RequestMapping(value = {"/authorize"})
    public Object authorize(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return oAuth2AuthzService.authorize(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/access_token"}, method = {RequestMethod.POST})
    public Object access_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return oAuth2AuthzService.access_token(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/userinfo"}, method = {RequestMethod.POST})
    public Object userinfo(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return oAuth2AuthzService.userinfo(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/logout"}, method = {RequestMethod.POST})
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return oAuth2AuthzService.logout_token(request, response);
    }

    @ResponseBody
    @RequestMapping(value = {"/refresh_token"}, method = {RequestMethod.POST})
    public Object refresh_token(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        return oAuth2AuthzService.refresh_token(request, response);
    }

}