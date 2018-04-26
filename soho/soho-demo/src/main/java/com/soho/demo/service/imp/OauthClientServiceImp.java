package com.soho.demo.service.imp;

import com.soho.demo.dao.OauthClientDAO;
import com.soho.demo.domain.OauthClient;
import com.soho.demo.service.OauthClientService;
import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.service.imp.BaseServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OauthClientServiceImp extends BaseServiceImp<OauthClient, Long> implements OauthClientService {
    @Autowired
    private OauthClientDAO oauthclientDAO;

    public MyBatisDAO<OauthClient, Long> getMybatisDAO() {
        return oauthclientDAO;
    }
}