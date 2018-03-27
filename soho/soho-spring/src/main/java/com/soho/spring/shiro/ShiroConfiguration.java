package com.soho.spring.shiro;

import com.soho.spring.model.ConfigData;
import com.soho.spring.shiro.filter.SimpleFormAuthenticationFilter;
import com.soho.spring.shiro.filter.SimpleRoleAuthorizationFilter;
import com.soho.spring.shiro.initialize.InitDefinition;
import com.soho.spring.shiro.initialize.ShiroInitializeService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
@Configuration
public class ShiroConfiguration {

    @Autowired(required = false)
    private CacheManager cacheManager;
    @Autowired(required = false)
    private ShiroInitializeService shiroInitializeService;
    @Autowired(required = false)
    private ConfigData configData;

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
        Map<String, Filter> map = shiroInitializeService.initFilters();
        if (!map.containsKey("authc")) {
            map.put("authc", new SimpleFormAuthenticationFilter(configData.getApiPrefix()));
        }
        if (!map.containsKey("role")) {
            map.put("role", new SimpleRoleAuthorizationFilter(configData.getApiPrefix(), configData.getRedirectUrl()));
        }
        shiroFilterFactoryBean.setFilters(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager initSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = shiroInitializeService.initRealms();
        securityManager.setRealms(realms);
        securityManager.setCacheManager(cacheManager);
        securityManager.setSessionManager(initSessionManager());
        return securityManager;
    }

    @Bean
    public SessionManager initSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        return defaultWebSessionManager;
    }

}