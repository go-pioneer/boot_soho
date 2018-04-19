package com.soho.demo;

import com.soho.spring.cache.CacheManager;
import com.soho.spring.extend.DefaultServletInitializer;
import com.soho.zookeeper.property.ZKPropertyConfigurer;
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
        // Zookeeper启动方式
        String filePath = "classpath:zookeeper.properties";
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password"};
        return new ZKPropertyConfigurer(filePath, decodeKeys);
        // return super.initPropertyPlaceholderConfigurer(); // 本地启动方式
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