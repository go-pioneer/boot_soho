package com.soho.demo.service.imp;

import com.soho.demo.dao.DogDAO;
import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.service.imp.BaseServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DogServiceImp extends BaseServiceImp<Dog, Long> implements DogService {
    @Autowired
    private DogDAO dogDAO;

    public MyBatisDAO<Dog, Long> getMybatisDAO() {
        return dogDAO;
    }
}