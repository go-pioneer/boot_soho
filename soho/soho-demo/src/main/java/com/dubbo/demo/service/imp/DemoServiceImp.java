package com.dubbo.demo.service.imp;

import com.alibaba.dubbo.config.annotation.Service;
import com.dubbo.demo.service.DemoService;
import com.soho.spring.model.ReqData;

import java.util.HashMap;
import java.util.Map;

@Service(version = "1.0.1")
public class DemoServiceImp implements DemoService {

    @Override
    public Object test(ReqData reqData) {
        Map map = new HashMap();
        map.put("username", "张三");
        return map;
    }

}
