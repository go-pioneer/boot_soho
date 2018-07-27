package com.soho.spring.configuration;

import com.soho.spring.extend.FastJsonHttpUTF8MessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class FastJsonConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        // 设置HTTP UTF-8 JSON对象模型
        return new HttpMessageConverters(new FastJsonHttpUTF8MessageConverter());
    }

}