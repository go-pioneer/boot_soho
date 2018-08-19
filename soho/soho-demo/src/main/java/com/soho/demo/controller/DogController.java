package com.soho.demo.controller;

import com.soho.cache.redisson.lock.RDLock;
import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
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
        // System.out.println(SessionUtils.getSessionId());
        long start = System.currentTimeMillis();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "123456");
        SecurityUtils.getSubject().login(token);
        Map<String, String> map = new HashMap<>();
        map.put("sessionId", SessionUtils.getSessionId());
        System.out.println("doReadSession---" + (System.currentTimeMillis() - start));
        return map;
    }

    @ResponseBody
    @RequestMapping("/test")
    public Object test(DogVO dogVO) throws BizErrorEx {
        return dogVO;
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
        Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        dogService.insert(dog);
        return dog;
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