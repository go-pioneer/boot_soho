package com.soho.spring.extend.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker自定义标签,HTML字符串转义
 *
 * @author shadow
 */
@Component
public class HtmlTag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws IOException {
        String content = StringUtils.isEmpty(params.get("content")) ? null : params.get("content").toString();
        if (!StringUtils.isEmpty(content)) {
            content = HtmlUtils.htmlUnescape(content.toString());
        }
        env.getOut().write(content);
    }

}