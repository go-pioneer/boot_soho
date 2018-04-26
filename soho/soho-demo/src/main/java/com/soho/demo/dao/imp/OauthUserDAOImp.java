package com.soho.demo.dao.imp;

import com.soho.demo.dao.OauthUserDAO;
import com.soho.demo.domain.OauthUser;
import com.soho.mybatis.crud.dao.imp.MyBatisDAOImp;
import org.springframework.stereotype.Repository;

@Repository
public class OauthUserDAOImp extends MyBatisDAOImp<OauthUser> implements OauthUserDAO {
}