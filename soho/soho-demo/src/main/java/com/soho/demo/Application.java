package com.soho.demo;

import com.soho.spring.cache.CacheManager;
import com.soho.spring.extend.JarServletInitializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shadow
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.soho"})
public class Application extends JarServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        // String filePath = "classpath:zookeeper.properties";
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password", "oss.appId", "oss.appKey", "ggk.appId", "ggk.appKey",};
        return super.initPropertyPlaceholderConfigurer(decodeKeys); // 本地启动方式
    }

    @Bean
    public CacheManager initCacheManager() {
        return super.initCacheManager(); // 本地缓存模式(EhCache)
    }

}