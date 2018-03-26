package com.soho.demo.dao;

import com.soho.demo.domain.Dog;
import com.soho.mybatis.crud.dao.MyBatisDAO;

public interface DogDAO extends MyBatisDAO<Dog, Long> {
}