package com.soho.demo;

import com.soho.spring.cache.CacheManager;
import com.soho.spring.extend.DefaultServletInitializer;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shadow
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.soho"})
public class Application2 extends DefaultServletInitializer {

    @Bean
    public PropertyPlaceholderConfigurer initPropertyPlaceholderConfigurer() {
        // Zookeeper启动方式
        // String filePath = "classpath:zookeeper.properties";
        String[] decodeKeys = new String[]{"spring.datasource.username", "spring.datasource.password", "oss.appId", "oss.appKey", "ggk.appId", "ggk.appKey",};
        // return new ZKPropertyConfigurer(filePath, decodeKeys);
        return super.initPropertyPlaceholderConfigurer(null, decodeKeys); // 本地启动方式
    }

    @Bean
    public CacheManager initCacheManager() {
        return super.initCacheManager(); // 本地缓存模式(EhCache)
    }

}