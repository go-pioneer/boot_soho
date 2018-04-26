package com.soho.demo.dao.imp;

import com.soho.demo.dao.OauthClientTokenDAO;
import com.soho.demo.domain.OauthClientToken;
import com.soho.mybatis.crud.dao.imp.MyBatisDAOImp;
import org.springframework.stereotype.Repository;

@Repository
public class OauthClientTokenDAOImp extends MyBatisDAOImp<OauthClientToken> implements OauthClientTokenDAO {
}