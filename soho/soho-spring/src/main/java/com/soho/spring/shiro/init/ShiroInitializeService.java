package com.soho.spring.shiro.init;

import org.apache.shiro.realm.Realm;

import java.util.List;

/**
 * @author shadow
 */
public interface ShiroInitializeService {

    // 初始化权限校验参数
    public InitDefinition initFilterChainDefinition();

    // 初始化认证权限校验器
    public List<Realm> initRealms();

}
