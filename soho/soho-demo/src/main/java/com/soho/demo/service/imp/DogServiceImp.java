package com.soho.demo.service.imp;

import com.soho.demo.dao.DogDAO;
import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.crud.dao.MyBatisDAO;
import com.soho.mybatis.crud.service.imp.BaseServiceImp;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.exception.MybatisDAOEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import com.soho.spring.cache.annotation.Cache;
import com.soho.spring.model.ReqData;
import com.soho.spring.mvc.model.FastMap;
import com.soho.rabbitmq.annotation.RabbiiMQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DogServiceImp extends BaseServiceImp<Dog, Long> implements DogService {
    @Autowired
    private DogDAO dogDAO;

    public MyBatisDAO<Dog, Long> getMybatisDAO() {
        return dogDAO;
    }

    public int insert(Dog entity) throws BizErrorEx {
        try {
            return getMybatisDAO().insert(entity);
        } catch (MybatisDAOEx e) {
            throw new BizErrorEx(e);
        }
    }

    @Cache(local_exp = 30, remote_exp = 60)
    @RabbiiMQ(channel = "SAVE_TO_MG", key = "dog")
    @Override
    public Object test(ReqData reqData) {
        Dog dog = new Dog();
        dog.setId(1l);
        try {
            Dog dog1 = dogDAO.findOneByCnd(new SQLCnd().eq("id", 2));
            if (dog1 == null) {
                dog1 = new Dog();
            }
            return new FastMap<>().add("result", "保存成功").add("dog", dog1).done();
        } catch (MybatisDAOEx mybatisDAOEx) {
            mybatisDAOEx.printStackTrace();
        }
        return null;
    }

}