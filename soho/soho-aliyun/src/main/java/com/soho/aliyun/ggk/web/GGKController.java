package com.soho.aliyun.ggk.web;

import com.aliyuncs.afs.model.v20180112.AuthenticateSigRequest;
import com.aliyuncs.afs.model.v20180112.AuthenticateSigResponse;
import com.soho.aliyun.ggk.utils.GGKUtils;
import com.soho.spring.model.OSSConfig;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.shiro.utils.KillRobotUtils;
import com.soho.spring.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/ggk")
public class GGKController {

    @Autowired
    private OSSConfig ossConfig;

    @RequestMapping("/init")
    public Object init(String callurl) {
        KillRobotUtils.release();
        return new FastView(ossConfig.getGgkPath()).add("callurl", StringUtils.isEmpty(callurl) ? "" : callurl).done();
    }

    @RequestMapping("/valid")
    public Object valid(HttpServletRequest httpRequest, String sessionid, String sig, String token, String scene, String callurl) {
        AuthenticateSigRequest request = new AuthenticateSigRequest();
        request.setSessionId(sessionid);// 必填参数，从前端获取，不可更改，android和ios只变更这个参数即可，下面参数不变保留xxx
        request.setSig(sig);// 必填参数，从前端获取，不可更改
        request.setToken(token);// 必填参数，从前端获取，不可更改
        request.setScene(scene);// 必填参数，从前端获取，不可更改
        request.setAppKey("FFFF000000000179AE04");// 必填参数，后端填写
        request.setRemoteIp(HttpUtils.getIpAddr(httpRequest));// 必填参数，后端填写
        try {
            AuthenticateSigResponse response = GGKUtils.iAcsClient.getAcsResponse(request);
            if (response.getCode() == 100) { // 验签通过
                KillRobotUtils.success();
                return new FastView("redirect:" + callurl).done();
            }
            callurl = StringUtils.isEmpty(callurl) ? "" : URLEncoder.encode(callurl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            callurl = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FastView("redirect:/ggk/init?callurl=" + callurl).done();
    }

}