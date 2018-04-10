package com.soho.shiro;

import com.soho.shiro.realm.WebLoginRealm;
import com.soho.spring.shiro.initialize.InitDefinition;
import com.soho.spring.shiro.initialize.ShiroInitializeService;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
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
        definition.setLoginUrl("/dog/login");
        definition.setSuccessUrl("/dog/findOne");
        definition.setUnauthorizedUrl("/403");
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/dog/findOne", "kickout,role[user]");
        definition.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return definition;
    }

    @Override
    public List<Realm> initRealms() {
        List<Realm> realms = new ArrayList<>();
        realms.add(new WebLoginRealm());
        return realms;
    }

    @Override
    public Map<String, Filter> initFilters() {
        return new LinkedHashMap<>();
    }

}
