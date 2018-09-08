package com.soho.extend.freemarker;

import com.soho.shiro.utils.SessionUtils;
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
 * FreeMarker自定义标签,节点权限控制
 *
 * @author shadow
 */
@Component
public class HasRoleTag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws TemplateException, IOException {
        Object role = params.get("role");
        if (StringUtils.isEmpty(role)) {
            throw new TemplateException("参数[id]不能为空", null);
        }
        if (hasRole(role)) {
            directiveBody.render(env.getOut());
        } else {
            env.getOut().write("");
        }

    }

    private boolean hasRole(Object role) {
        String[] roles = role.toString().split(",");
        Subject subject = SessionUtils.getSubject();
        for (String r : roles) {
            if (subject.hasRole(r)) {
                return true;
            }
        }
        return false;
    }

}  