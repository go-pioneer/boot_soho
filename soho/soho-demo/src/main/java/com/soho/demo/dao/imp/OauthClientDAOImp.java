package com.soho.demo.dao.imp;

import com.soho.demo.dao.OauthClientDAO;
import com.soho.demo.domain.OauthClient;
import com.soho.mybatis.crud.dao.imp.MyBatisDAOImp;
import org.springframework.stereotype.Repository;

@Repository
public class OauthClientDAOImp extends MyBatisDAOImp<OauthClient> implements OauthClientDAO {
}