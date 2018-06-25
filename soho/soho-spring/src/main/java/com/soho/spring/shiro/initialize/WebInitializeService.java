package com.soho.spring.shiro.initialize;

import freemarker.template.TemplateDirectiveModel;
import org.apache.shiro.realm.Realm;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.Filter;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
public interface WebInitializeService {

    // 初始化权限校验参数
    public InitDefinition initShiroFilterChainDefinition();

    // 初始化认证权限校验器
    public List<Realm> initShiroRealms();

    // 初始化认证拦截器
    public Map<String, Filter> initShiroFilters();

    // 开启HTTPS Cookie数据安全
    public boolean isHttpsCookieSecure();

    // 初始化web拦截器
    public List<HandlerInterceptor> initWebMVCInterceptor();

    // 初始化freemarker标签
    public Map<String, TemplateDirectiveModel> initFreeMarkerTag();

}
