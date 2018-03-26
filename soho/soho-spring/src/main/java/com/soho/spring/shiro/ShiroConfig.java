package com.soho.spring.shiro;

import com.soho.spring.shiro.init.InitDefinition;
import com.soho.spring.shiro.init.ShiroInitializeService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author shadow
 */
@Configuration
public class ShiroConfig {

    @Autowired(required = false)
    private CacheManager cacheManager;
    @Autowired(required = false)
    private ShiroInitializeService shiroInitializeService;

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean initShirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        InitDefinition definition = shiroInitializeService.initFilterChainDefinition();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(definition.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(definition.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(definition.getUnauthorizedUrl());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(definition.getFilterChainDefinitionMap());
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager initSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = shiroInitializeService.initRealms();
        securityManager.setRealms(realms);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(new DefaultWebSessionManager());
        return securityManager;
    }

}