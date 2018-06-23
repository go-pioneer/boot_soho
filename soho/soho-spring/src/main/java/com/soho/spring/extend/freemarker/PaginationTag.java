package com.soho.spring.extend.freemarker;

import com.soho.spring.utils.PaginationUtils;
import com.soho.spring.utils.RGXUtils;
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
 * FreeMarker自定义标签,分页导航
 *
 * @author shadow
 */
@Component
public class PaginationTag implements TemplateDirectiveModel {

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody directiveBody)
            throws TemplateException, IOException {
        Object pageFun = params.get("fn");
        Object pageNo = params.get("pageNo");
        Object pageSize = params.get("pageSize");
        Object pageNumber = params.get("pageNumber");
        if (pageFun == null || StringUtils.isEmpty(pageFun.toString())) {
            throw new TemplateException("参数[fn]非法", null);
        }
        if (!RGXUtils.isInteger(pageNo == null ? "" : pageNo)) {
            throw new TemplateException("参数[pageNo]非法", null);
        }
        if (!RGXUtils.isInteger(pageSize == null ? "" : pageSize)) {
            throw new TemplateException("参数[pageSize]非法", null);
        }
        if (!RGXUtils.isInteger(pageNumber == null ? "" : pageNumber)) {
            throw new TemplateException("参数[pageNumber]非法", null);
        }
        env.getOut().write(PaginationUtils.getHtml(pageFun.toString(), Integer.parseInt(pageNo.toString()), Integer.parseInt(pageSize.toString()), Integer.parseInt(pageNumber.toString())));
    }

}