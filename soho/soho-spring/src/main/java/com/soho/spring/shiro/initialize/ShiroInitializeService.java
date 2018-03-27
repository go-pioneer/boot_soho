package com.soho.spring.shiro.initialize;

import org.apache.shiro.realm.Realm;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
public interface ShiroInitializeService {

    // 初始化权限校验参数
    public InitDefinition initFilterChainDefinition();

    // 初始化认证权限校验器
    public List<Realm> initRealms();

    // 初始化认证拦截器
    public Map<String, Filter> initFilters();

}
