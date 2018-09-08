package com.soho.extend.freemarker;

import com.soho.spring.model.OSSConfig;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker自定义标签,填充静态资源域名
 *
 * @author shadow
 */
@Component
public class OSSDomainTag implements TemplateDirectiveModel {

    @Autowired
    private OSSConfig ossConfig;

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws IOException {
        env.getOut().write(ossConfig.getDomain());
    }

}