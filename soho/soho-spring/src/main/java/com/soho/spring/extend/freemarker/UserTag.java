package com.soho.spring.extend.freemarker;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soho.spring.shiro.utils.SessionUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * FreeMarker自定义标签,会话用户对象
 *
 * @author shadow
 */
@Component
public class UserTag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws TemplateException, IOException {
        String content = "";
        Object key = params.get("key");
        if (!StringUtils.isEmpty(key)) {
            Object object = SessionUtils.getUser();
            if (object != null) {
                JSONObject user = JSON.parseObject(JSON.toJSONString(object));
                if (!StringUtils.isEmpty(user.getString(key.toString()))) {
                    content = user.getString(key.toString());
                }
            }
        }
        env.getOut().write(content);
    }
}