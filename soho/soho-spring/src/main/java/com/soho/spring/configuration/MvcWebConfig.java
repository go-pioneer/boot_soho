package com.soho.spring.configuration;

import com.soho.spring.mvc.filter.SafetyFilter;
import com.soho.spring.mvc.interceptor.RequestInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shadow
 */
@Configuration
public class MvcWebConfig implements WebMvcConfigurer {

    @Bean
    public RequestInterceptor getSecurityInterceptor() {
        return new RequestInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        // 排除配置
        // addInterceptor.excludePathPatterns("/error");
        // addInterceptor.excludePathPatterns("/login**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SafetyFilter());
        registration.addUrlPatterns("/*");//设置过滤路径，/*所有路径
        // registration.addInitParameter("name", "alue");//添加默认参数
        registration.setName("SafetyFilter");//设置拦截器名称
        registration.setOrder(1);//设置优先级
        return registration;
    }

}