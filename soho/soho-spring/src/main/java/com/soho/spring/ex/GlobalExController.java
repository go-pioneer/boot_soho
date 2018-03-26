package com.soho.spring.ex;

import com.alibaba.fastjson.JSON;
import com.soho.spring.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
@Controller
@RequestMapping("/error")
public class GlobalExController {

    @Value("${system.errorPage}")
    private String errorPage;

    @RequestMapping("/errorPage")
    public ModelAndView errorPage(ExModel model) {
        ModelAndView view = new ModelAndView(errorPage);
        view.addObject("model", model);
        return view;
    }

    @RequestMapping("/errorJson")
    public void json(HttpServletResponse response, ExModel model) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("code", model.getCode());
        map.put("msg", model.getMsg());
        map.put("callurl", model.getCallurl());
        response.setCharacterEncoding(HttpUtils.UTF8);
        response.setContentType("application/json; charset=utf-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.print(JSON.toJSONString(map));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}