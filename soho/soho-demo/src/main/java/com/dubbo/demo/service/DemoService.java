package com.dubbo.demo.service;

import com.soho.demo.domain.Dog;
import com.soho.mybatis.crud.service.BaseService;
import com.soho.spring.model.ReqData;

public interface DemoService {

    public Object test(ReqData reqData);

}