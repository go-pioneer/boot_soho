package com.soho.spring.extend.freemarker;

import com.soho.spring.model.OSSData;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

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
    private OSSData ossData;

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws IOException {
        env.getOut().write(ossData.getDomain());
    }

}