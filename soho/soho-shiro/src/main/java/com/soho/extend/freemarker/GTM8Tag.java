package com.soho.extend.freemarker;

import com.soho.spring.utils.DateUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker自定义标签,GTM8时间戳转换
 *
 * @author shadow
 */
@Component
public class GTM8Tag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws TemplateException, IOException {
        Object time = params.get("time");
        if (!StringUtils.isEmpty(time) && !"0".equals(time.toString())) {
            env.getOut().write(DateUtils.GMT8_S(Long.parseLong(time.toString())));
        } else {
            env.getOut().write("");
        }
    }

}