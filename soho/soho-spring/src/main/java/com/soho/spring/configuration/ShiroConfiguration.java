package com.soho.spring.configuration;

import com.soho.spring.model.DeftConfig;
import com.soho.spring.model.ErrorPageConfig;
import com.soho.spring.shiro.factory.DefaultShiroFilterFactoryBean;
import com.soho.spring.shiro.filter.SimpleFormAuthenticationFilter;
import com.soho.spring.shiro.filter.SimpleKickOutSessionFilter;
import com.soho.spring.shiro.filter.SimpleRoleAuthorizationFilter;
import com.soho.spring.shiro.initialize.InitDefinition;
import com.soho.spring.shiro.initialize.RuleChain;
import com.soho.spring.shiro.initialize.WebInitializeService;
import com.soho.spring.shiro.session.ShiroSessionDAO;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    private WebInitializeService webInitializeService;
    @Autowired(required = false)
    private DeftConfig deftConfig;
    @Autowired(required = false)
    private ErrorPageConfig errorPageConfig;

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean initShiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new DefaultShiroFilterFactoryBean();
        InitDefinition definition = webInitializeService.initShiroFilterChainDefinition();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(definition.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(definition.getSuccessUrl());
        shiroFilterFactoryBean.setUnauthorizedUrl(definition.getUnauthorizedUrl());
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        for (RuleChain ruleChain : definition.getAnonRuleChains()) {
            filterChainDefinitionMap.put(ruleChain.getUrl(), ruleChain.getRole());
        }
        for (RuleChain ruleChain : definition.getRoleRuleChains()) {
            filterChainDefinitionMap.put(ruleChain.getUrl(), ruleChain.getRole());
        }
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        Map<String, Filter> map = webInitializeService.initShiroFilters();
        if (!map.containsKey("authc")) {
            map.put("authc", new SimpleFormAuthenticationFilter(deftConfig.getApiPrefix()));
        }
        if (!map.containsKey("role")) {
            map.put("role", new SimpleRoleAuthorizationFilter(deftConfig.getApiPrefix(), errorPageConfig.getError403()));
        }
        if (!map.containsKey("kickout")) {
            map.put("kickout", new SimpleKickOutSessionFilter(deftConfig.getApiPrefix()));
        }
        shiroFilterFactoryBean.setFilters(map);
        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager initSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = webInitializeService.initShiroRealms();
        securityManager.setRealms(realms);
        securityManager.setCacheManager(cacheManager);
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        List<String> anonUrls = new ArrayList<>();
        for (RuleChain ruleChain : webInitializeService.initAnonRuleChains()) {
            anonUrls.add(ruleChain.getUrl());
        }
        defaultWebSessionManager.setSessionDAO(new ShiroSessionDAO(anonUrls));
        Cookie cookie = defaultWebSessionManager.getSessionIdCookie();
        cookie.setSecure(webInitializeService.isHttpsCookieSecure());
        securityManager.setSessionManager(defaultWebSessionManager);
        return securityManager;
    }

}