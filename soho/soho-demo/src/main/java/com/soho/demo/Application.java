package com.soho.demo;

import com.soho.spring.cache.CacheManager;
import com.soho.spring.extend.DefaultServletInitializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shadow
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.soho"})
public class Application extends DefaultServletInitializer {

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        // return super.initDefaultPropertyPlaceholderConfigurer(); // 本地启动方式
        return super.initZookeeperPropertyPlaceholderConfigurer(); // Zookeeper启动方式
    }

    @Bean
    public HttpMessageConverters initFastjsonHttpMessageConverter() {
        return super.initFastjsonHttpMessageConverter(); // HTTP数据序列化模式(alibaba.fastjson)
    }

    @Bean
    public CacheManager initCacheManager() {
        return super.initCacheManager(); // 本地缓存模式(EhCache)
    }

}