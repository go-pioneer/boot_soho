package com.soho.shiro.configuration;

import com.soho.extend.freemarker.*;
import com.soho.shiro.initialize.WebInitializeService;
import com.soho.spring.extend.HtmlTemplateLoader;
import freemarker.template.TemplateDirectiveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.Map;

/**
 * @author shadow
 */
@Configuration
public class FreemarkerConfiguration {

    @Autowired(required = false)
    private WebInitializeService webInitializeService;
    @Autowired(required = false)
    private freemarker.template.Configuration configuration;
    @Autowired(required = false)
    private HtmlTag htmlTag;
    @Autowired(required = false)
    private GTM8Tag gtm8Tag;
    @Autowired(required = false)
    private HasRoleTag hasRoleTag;
    @Autowired(required = false)
    private UserTag userTag;
    @Autowired(required = false)
    private OSSDomainTag ossDomainTag;
    @Autowired(required = false)
    private TokenTag tokenTag;
    @Autowired(required = false)
    private PaginationTag paginationTag;


    @PostConstruct
    public void setSharedVariable() {
        configuration.setTemplateLoader(new HtmlTemplateLoader(configuration.getTemplateLoader()));
        // 基本设置
        configuration.setNumberFormat("#.####");
        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setLocale(new Locale("zh_CN"));
        // 基本标签
        configuration.setSharedVariable("html", htmlTag);
        configuration.setSharedVariable("gtm8", gtm8Tag);
        configuration.setSharedVariable("hasRole", hasRoleTag);
        configuration.setSharedVariable("usr", userTag);
        configuration.setSharedVariable("formToken", tokenTag);
        configuration.setSharedVariable("OSSDomain", ossDomainTag);
        configuration.setSharedVariable("pagination", paginationTag);
        Map<String, TemplateDirectiveModel> tagMap = webInitializeService.initFreeMarkerTag();
        if (tagMap != null && !tagMap.isEmpty()) {
            for (Map.Entry<String, TemplateDirectiveModel> entry : tagMap.entrySet()) {
                configuration.setSharedVariable(entry.getKey().trim(), entry.getValue());
            }
        }
    }

}