package com.soho.spring.configuration;

import com.soho.spring.model.ConfigData;
import com.soho.spring.mvc.filter.SafetyFilter;
import com.soho.spring.mvc.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

/**
 * @author shadow
 */
@Configuration
public class MvcWebConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private ConfigData configData;

    @Bean
    public RequestInterceptor getSecurityInterceptor() {
        return new RequestInterceptor();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(configData.getMaxFileSize()); // 设置单个文件大小
        factory.setMaxRequestSize(configData.getMaxRequestSize()); // 设置总上传数据总大小
        // factory.setLocation(configData.getUploadPath()); // 设置临时文件上传保存路径
        return factory.createMultipartConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());
        // 排除配置
        // addInterceptor.excludePathPatterns("/login**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    @Bean
    public FilterRegistrationBean testFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SafetyFilter());
        registration.setName("SafetyFilter"); // 设置拦截器名称
        registration.addInitParameter("jsoupPrefix", configData.getJsoupPrefix()); // 添加默认参数
        registration.addUrlPatterns("/*"); // 设置过滤路径，/*所有路径
        registration.setOrder(1); // 设置优先级
        return registration;
    }

}