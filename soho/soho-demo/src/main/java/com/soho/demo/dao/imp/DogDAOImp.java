package com.soho.demo.dao.imp;

import com.soho.demo.dao.DogDAO;
import com.soho.demo.domain.Dog;
import com.soho.mybatis.crud.dao.imp.MyBatisDAOImp;
import org.springframework.stereotype.Repository;

@Repository
public class DogDAOImp extends MyBatisDAOImp<Dog> implements DogDAO {
}