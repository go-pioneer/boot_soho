package com.soho.demo.service;

import com.soho.demo.domain.Dog;
import com.soho.mybatis.crud.service.BaseService;
import com.soho.spring.model.ReqData;

public interface DogService extends BaseService<Dog, Long> {

    public Object test(ReqData reqData);

}