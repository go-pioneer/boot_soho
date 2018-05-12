package com.soho.spring.extend.freemarker;

import com.soho.spring.shiro.utils.FormTokenUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker自定义标签,Token防重复提交
 *
 * @author shadow
 */
@Component
public class TokenTag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws IOException {
        env.getOut().write(FormTokenUtils.addFormToken());
    }

}