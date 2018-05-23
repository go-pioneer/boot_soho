package com.soho.spring.security.web;

import com.soho.mybatis.exception.BizErrorEx;
import com.soho.spring.model.RetCode;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.shiro.utils.FormTokenUtils;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.spring.utils.NumUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class FormTokenController {

    @ResponseBody
    @RequestMapping("/form/token")
    public Object formFoken() throws BizErrorEx {
        if (!SessionUtils.getSubject().isAuthenticated()) {
            throw new BizErrorEx(RetCode.BIZ_ERROR_STATUS, "请先登录");
        }
        String form_sn = NumUtils.getIntSN(6);
        String form_token = NumUtils.getStrSN();
        SessionUtils.setAttribute(form_sn, form_token);
        return new FastMap<>()
                .add(FormTokenUtils.SECURITY_FORM_SN, form_sn)
                .add(FormTokenUtils.SECURITY_FORM_TOKEN, form_token)
                .done();
    }

}