package com.soho.demo.controller;

import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/dog")
public class DogController {

    @Autowired
    private DogService dogService;

    @ResponseBody
    @RequestMapping("/save")
    public Object save() throws BizErrorEx {
        Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        dogService.insert(dog);
        return dog;
    }

    @ResponseBody
    @RequestMapping("/findOne")
    public Object findOne() throws BizErrorEx {
        Dog dog = dogService.findOneByCnd(new SQLCnd().eq("id", 2));
        if (dog == null) {
            dog = new Dog();
        }
        return dog;
    }

    @ResponseBody
    @RequestMapping("/findAll")
    public Object findAll() throws BizErrorEx {
        return dogService.findAll();
    }

}