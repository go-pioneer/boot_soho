package com.soho.shiro;

import com.soho.shiro.realm.MyShiroRealm;
import com.soho.spring.shiro.cache.SimpleShiroCacheManager;
import com.soho.spring.shiro.init.InitDefinition;
import com.soho.spring.shiro.init.ShiroInitializeService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
@Component
public class ShiroInitializeServiceImp implements ShiroInitializeService {

    @Override
    public InitDefinition initFilterChainDefinition() {
        InitDefinition definition = new InitDefinition();
        definition.setLoginUrl("/login");
        definition.setSuccessUrl("/index");
        definition.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/font-awesome/**", "anon");
        filterChainDefinitionMap.put("/**", "authc");
        definition.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return definition;
    }

    @Override
    public List<Realm> initRealms() {
        List<Realm> realms = new ArrayList<>();
        realms.add(new MyShiroRealm());
        return realms;
    }

}
