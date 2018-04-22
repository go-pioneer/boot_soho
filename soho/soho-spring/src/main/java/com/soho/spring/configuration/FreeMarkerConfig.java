package com.soho.spring.configuration;

import com.soho.spring.extend.HtmlTemplateLoader;
import com.soho.spring.extend.freemarker.HtmlTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author shadow
 */
@Configuration
public class FreeMarkerConfig {

    @Autowired(required = false)
    private freemarker.template.Configuration configuration;
    @Autowired(required = false)
    private HtmlTag htmlTag;


    @PostConstruct
    public void setSharedVariable() {
        configuration.setTemplateLoader(new HtmlTemplateLoader(configuration.getTemplateLoader()));
        configuration.setSharedVariable("html", htmlTag);
    }

}