package com.soho.shiro;

import com.soho.aliyun.ggk.interceptor.KillRobotInterceptor;
import com.soho.shiro.realm.WebLoginRealm;
import com.soho.spring.model.DbConfig;
import com.soho.spring.model.HikariDS;
import com.soho.spring.mvc.interceptor.FormTokenInterceptor;
import com.soho.spring.shiro.initialize.InitDefinition;
import com.soho.spring.shiro.initialize.RuleChain;
import com.soho.spring.shiro.initialize.WebInitializeService;
import com.soho.spring.utils.WCCUtils;
import freemarker.template.TemplateDirectiveModel;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.*;

/**
 * @author shadow
 */
@Component
public class WebInitializeServiceImp implements WebInitializeService {

    @Autowired
    private DbConfig dbConfig;

    @Override
    public InitDefinition initShiroFilterChainDefinition() {
        InitDefinition definition = new InitDefinition();
        definition.setLoginUrl("/dog/login");
        definition.setSuccessUrl("/dog/findOne");
        definition.setUnauthorizedUrl("/403");
        definition.setAnonRuleChains(initAnonRuleChains());
        definition.setRoleRuleChains(WCCUtils.ruleChainComparator(initDeftRuleChains()));
        return definition;
    }

    @Override
    public List<RuleChain> initAnonRuleChains() {
        List<RuleChain> anonRuleChains = new ArrayList<>();
        anonRuleChains.add(new RuleChain("/static/**", "anon"));
        anonRuleChains.add(new RuleChain("/ggk/**", "anon"));
        return anonRuleChains;
    }

    @Override
    public List<RuleChain> initDeftRuleChains() {
        List<RuleChain> roleRuleChains = new ArrayList<>();
        // roleRuleChains.add(new RuleChain("/dog/findOne", "kickout,role[user]"));
        // roleRuleChains.add(new RuleChain("/dog/findAll", "authc"));
        return roleRuleChains;
    }

    @Override
    public List<RuleChain> initDBRuleChains() {
        return new ArrayList<>();
    }

    @Override
    public List<Realm> initShiroRealms() {
        List<Realm> realms = new ArrayList<>();
        realms.add(new WebLoginRealm());
        return realms;
    }

    @Override
    public Map<String, Filter> initShiroFilters() {
        return new LinkedHashMap<>();
    }

    @Override
    public boolean isHttpsCookieSecure() {
        return false;
    }

    @Override
    public List<HandlerInterceptor> initWebMVCInterceptor() {
        List<HandlerInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new KillRobotInterceptor());
        interceptors.add(new FormTokenInterceptor());
        return interceptors;
    }

    @Override
    public Map<String, TemplateDirectiveModel> initFreeMarkerTag() {
        return new HashMap<>();
    }

    @Override
    public List<HikariDS> initOtherDataSource() {
        HikariDS ds = new HikariDS("DB_SLAVE", dbConfig.getDriverClassName(), dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
        List<HikariDS> dsList = new ArrayList<>();
        dsList.add(ds);
        return dsList;
    }

}
