package com.soho.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dubbo.demo.service.DemoService;
import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import com.soho.spring.cache.annotation.RDLock;
import com.soho.spring.model.ReqData;
import com.soho.spring.mvc.annotation.FormToken;
import com.soho.spring.mvc.annotation.KillRobot;
import com.soho.spring.mvc.model.FastMap;
import com.soho.spring.mvc.model.FastView;
import com.soho.spring.shiro.utils.SessionUtils;
import com.soho.spring.utils.FileUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dog")
public class DogController {

    @Autowired
    private DogService dogService;

    @ResponseBody
    @RequestMapping("/login")
    public Object login(String username, String password) throws BizErrorEx {
        try {
            // System.out.println(SessionUtils.getSessionId());
            long start = System.currentTimeMillis();
            UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "123456");
            System.out.println("login0---" + (System.currentTimeMillis() - start));
            SecurityUtils.getSubject().login(token);
            System.out.println("login1---" + (System.currentTimeMillis() - start));
            Map<String, String> map = new HashMap<>();
            map.put("sessionId", SessionUtils.getSessionId());
            SessionUtils.getSession();
            System.out.println("login2---" + (System.currentTimeMillis() - start));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    @ResponseBody
    @RequestMapping("/test")
    public Object test(DogVO dogVO) throws BizErrorEx {
        return dogService.test(dogVO);
    }

    @ResponseBody
    @RequestMapping("/logout")
    public Object logout() {
        SessionUtils.logout();
        return new FastMap<>().add("sessionId", SessionUtils.getSessionId()).done();
    }

    @KillRobot
    @RequestMapping("/index")
    public Object index(String username) {
        return new FastView("/index").add("username", username).done();
    }

    @KillRobot(goback = "/dog/index", reset = true)
    @FormToken
    @ResponseBody
    @RequestMapping("/upload")
    public Object upload(String username, MultipartFile file) throws BizErrorEx {
        return FileUtils.uploadImageByReSize(file, 500, 500, "1", true);
    }

    @ResponseBody
    @RequestMapping("/save")
    public Object save() throws BizErrorEx {
        try {
            long l = System.currentTimeMillis();
            Object object = dogService.test(new ReqData());
            System.out.println("------" + (System.currentTimeMillis() - l));
            Dog dog = new Dog();
            dog.setName("小狗");
            dog.setAge(5);
            dog.setSex(1);
            dog.setCtime(System.currentTimeMillis());
            dog.setState(1);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        /*Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        dogService.insert(dog);
        long l = System.currentTimeMillis();
        String s = MD5Utils.encrypt(JSON.toJSONString(dog), null);
        System.out.println("------" + (System.currentTimeMillis() - l));
        return new FastMap<>().add("md5", s).done();*/
    }

    @ResponseBody
    @RequestMapping("/webflux")
    public Mono<Dog> webflux() throws BizErrorEx {
        Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        return Mono.just(dog);
    }

    int i = 0;

    @RDLock
    @ResponseBody
    @RequestMapping("/findOne")
    public Object findOne() throws BizErrorEx {
        Dog dog = dogService.findOneByCnd(new SQLCnd().eq("id", 2));
        if (dog == null) {
            dog = new Dog();
        }
        i++;
        System.out.println(i);
        return dog;
    }


    @ResponseBody
    @RequestMapping("/findAll")
    public Object findAll() throws BizErrorEx {
//        List list = dogService
//                .findMapByCnd(new SQLCnd().fields("a.id", "a.name")
//                        .from("dog a")
//                        .join(new Join(MODE.LEFT, "color b").on("a.id", "b.id").on("a.id", 1))
//                        .join(new Join(MODE.LEFT, "color c").on("a.id", "c.id"))
//                        .join(new Join(MODE.INNER, "color d").on("a.id", "d.id"))
//                        .eq("a.id", 2));
//        System.out.println(list);
//        dogService.test(new ReqData());
        return dogService.findByCnd(new SQLCnd().eq("id", 2));
    }

}