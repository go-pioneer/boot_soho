package com.soho.spring.configuration;

import com.soho.spring.extend.FastJsonHttpUTF8MessageConverter;
import com.soho.spring.model.DeftConfig;
import com.soho.spring.model.ErrorPageConfig;
import com.soho.spring.model.OSSConfig;
import com.soho.spring.mvc.filter.SafetyFilter;
import com.soho.spring.mvc.interceptor.ErrorPageInterceptor;
import com.soho.spring.shiro.initialize.WebInitializeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;
import java.util.List;

/**
 * @author shadow
 */
@Configuration
public class SpringMvcConfiguration implements WebMvcConfigurer {

    @Autowired(required = false)
    private DeftConfig deftConfig;
    @Autowired(required = false)
    private ErrorPageConfig errorPageConfig;
    @Autowired(required = false)
    private WebInitializeService webInitializeService;

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(deftConfig.getMaxFileSize()); // 设置单个文件大小
        factory.setMaxRequestSize(deftConfig.getMaxRequestSize()); // 设置总上传数据总大小
        return factory.createMultipartConfig();
    }

    @Bean
    public ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }

    @Bean
    public FilterRegistrationBean safeFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SafetyFilter());
        registration.setName("safetyFilter"); // 设置拦截器名称
        registration.addInitParameter("jsoupPrefix", deftConfig.getJsoupPrefix()); // 添加默认参数
        registration.addUrlPatterns("/*"); // 设置过滤路径，/*所有路径
        registration.setOrder(1); // 设置优先级
        return registration;
    }

    @Bean
    public FilterRegistrationBean errorPageFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(errorPageFilter());
        registration.setEnabled(false);
        return registration;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 默认错误页面拦截器处理
        InterceptorRegistration error = registry.addInterceptor(new ErrorPageInterceptor(deftConfig, errorPageConfig));
        error.excludePathPatterns(deftConfig.getStaticPrefix() + "**"); // 排除配置
        error.addPathPatterns("/**"); // 拦截配置
        List<HandlerInterceptor> interceptors = webInitializeService.initWebMVCInterceptor();
        for (HandlerInterceptor interceptor : interceptors) {
            InterceptorRegistration addInterceptor = registry.addInterceptor(interceptor);
            addInterceptor.excludePathPatterns("/static/**"); // 排除配置
            addInterceptor.addPathPatterns("/**"); // 拦截配置
        }
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FastJsonHttpUTF8MessageConverter()); // 设置HTTP UTF-8 JSON对象模型
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        if (!StringUtils.isEmpty(deftConfig.getHomePage())) {
            registry.addViewController("/").setViewName("forward:" + deftConfig.getHomePage());
            registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        }
    }

}