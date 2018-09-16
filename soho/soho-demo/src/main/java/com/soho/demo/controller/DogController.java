package com.soho.demo.controller;

import com.soho.demo.domain.Dog;
import com.soho.demo.service.DogService;
import com.soho.mybatis.exception.BizErrorEx;
import com.soho.mybatis.sqlcode.condition.imp.SQLCnd;
import com.soho.spring.cache.annotation.RDLock;
import com.soho.spring.model.ReqData;
import com.soho.spring.model.RetData;
import com.soho.spring.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dog")
public class DogController {

    @Autowired
    private DogService dogService;

    /*@ResponseBody
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
    }*/

    private static Map<String, AsyncContext> map = new HashMap<>();
    public static String nonce = "test";

    @ResponseBody
    @RequestMapping("/test1")
    public void test1(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        AsyncContext context = map.get(nonce);
        HttpUtils.responseJsonData((HttpServletResponse) context.getResponse(), new RetData<>(new HashMap<>()));
        context.complete();
    }

    @ResponseBody
    @RequestMapping("/test")
    public void test(HttpServletRequest request, HttpServletResponse response) throws BizErrorEx {
        AsyncContext context = request.startAsync();
        context.setTimeout(10000);
        context.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent asyncEvent) throws IOException {
                System.out.println("---已完成处理");
                Object nonce = asyncEvent.getAsyncContext().getRequest().getAttribute(DogController.nonce);
                if (nonce != null && map.get(nonce) != null) {
                    map.remove(nonce);
                    System.out.println("---删除持久化context: " + nonce);
                }
            }

            @Override
            public void onTimeout(AsyncEvent asyncEvent) throws IOException {
                System.out.println("---onTimeout");
                AsyncContext asyncContext = asyncEvent.getAsyncContext();
                HttpUtils.responseJsonData((HttpServletResponse) asyncContext.getResponse(), new RetData<>("onTimeout"));
                asyncContext.complete();
            }

            @Override
            public void onError(AsyncEvent asyncEvent) throws IOException {
                System.out.println("---onError");
                AsyncContext asyncContext = asyncEvent.getAsyncContext();
                HttpUtils.responseJsonData((HttpServletResponse) asyncContext.getResponse(), new RetData<>("onError"));
                asyncContext.complete();
            }

            @Override
            public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
                System.out.println("---onStartAsync");
            }
        });
        map.put(nonce, context);
        new Thread(new Runnable() {
            int i = 0;

            @Override
            public void run() {
                request.setAttribute(nonce, nonce);
                while (true) {
                    System.out.println(request.isAsyncStarted() + "---");
                    if (!request.isAsyncStarted()) {
                        System.out.println("---退出循环检测");
                        return;
                    }
                    i++;
                    try {
                        Thread.sleep(200);
                        System.out.println("---" + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
    public Flux<Dog> webflux() {
        System.err.println("findAllCity 1");
        Flux<Dog> flux = Flux.create(dogFluxSink -> {
            try {
                dogService.findAll().forEach(dog -> dogFluxSink.next(dog));
                // dogFluxSink.next(dogService.findById(1l));
            } catch (BizErrorEx ex) {
                ex.printStackTrace();
            }
            dogFluxSink.complete();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("findAllCity 2");
        });
        System.err.println("findAllCity  end 3");
        return flux;

        /*Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        return Mono.just(dog);*/
    }

    @ResponseBody
    @RequestMapping("/webflux2")
    public Mono<Dog> save1(String id) throws BizErrorEx {
        System.err.println("findOneCity 1" + id);
        Dog dog = new Dog();
        dog.setName("小狗");
        dog.setAge(5);
        dog.setSex(1);
        dog.setCtime(System.currentTimeMillis());
        dog.setState(1);
        dogService.insert(dog);
        Mono mono = Mono.create(cityMonoSink -> {
            try {
                Dog object = dogService.findById(dog.getId());
                cityMonoSink.success(object);
                System.err.println("findOneCity 2");
                Integer.parseInt("s");
            } catch (Exception ex) {
                ex.printStackTrace();
                cityMonoSink.error(ex);
            }
        });
        System.err.println("findOneCity 4");
        return mono;
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