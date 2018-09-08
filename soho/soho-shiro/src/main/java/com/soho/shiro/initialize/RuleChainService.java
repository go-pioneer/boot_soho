package com.soho.shiro.initialize;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RuleChainService {

    @Autowired(required = false)
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired(required = false)
    private WebInitializeService webInitializeService;

    public void reload() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 获取过滤管理器
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
            // 清空初始权限配置
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 重新构建生成,加载自定义权限配置
            List<RuleChain> anonRuleChains = webInitializeService.initAnonRuleChains();
            if (anonRuleChains == null) {
                anonRuleChains = new ArrayList<>();
            }
            List<RuleChain> deftRuleChains = webInitializeService.initDeftRuleChains();
            if (deftRuleChains != null) {
                anonRuleChains.addAll(deftRuleChains);
            }
            for (RuleChain chain : anonRuleChains) {
                manager.createChain(chain.getUrl(), chain.getRole());
            }
        }
    }

}