package com.soho.mvc.filter;

import com.soho.mvc.wrapper.HttpRequestWrapper;
import com.soho.spring.utils.JsoupUtils;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
public class SafetyFilter implements Filter {

    private final static int max_parameter_size = 50; // 最大参数数量
    private final static int max_field_len = 25; // 最大参数名长度
    private final static int max_querystring_len = 1000; // 最大GET参数名长度
    private final static int max_value_len = 25000; // 最大参数值长度
    private Map<String, List<String>> jsoupPrefix = new HashMap<>(); // 富文本过滤,指定请求方法路径,参数名

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain
            filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null && query.length() > max_querystring_len) {
            response.sendError(403);
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        if (map == null || map.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        } else {
            if (map.size() > max_parameter_size) {
                response.sendError(403);
                return;
            }
        }
        HttpRequestWrapper wrapper = new HttpRequestWrapper(request);
        copyParameterMap(wrapper, uri, map);
        filterChain.doFilter(wrapper, response);
    }

    private void copyParameterMap(HttpRequestWrapper wrapper, String uri, Map<String, String[]> map) {
        boolean isJsoup = false;
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (StringUtils.isEmpty(key) || key.length() > max_field_len) {
                continue;
            }
            List<String> fields = jsoupPrefix.get(uri);
            if (fields != null && !fields.isEmpty() && fields.contains(entry.getKey())) {
                isJsoup = true;
            }
            for (String value : values) {
                if (!StringUtils.isEmpty(value) && value.length() > max_value_len) {
                    continue;
                }
                if (!StringUtils.isEmpty(value) && isJsoup) {
                    wrapper.addParameter(key, JsoupUtils.safety(value));
                } else {
                    wrapper.addParameter(key, value);
                }
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String prefix = config.getInitParameter("jsoupPrefix"); // 拦截数据格式 url|field1|field2|field3
        prefix = prefix == null ? "" : prefix;
        String[] prefixs = prefix.split(",");
        for (String str : prefixs) {
            String[] array = str.split("\\|");
            if (array.length < 1) {
                continue;
            }
            String uri = null;
            List<String> fields = new ArrayList<>(array.length - 1);
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    uri = array[i].trim();
                } else {
                    fields.add(array[i].trim());
                }
            }
            if (!StringUtils.isEmpty(uri) && !jsoupPrefix.containsKey(uri) || !fields.isEmpty()) { // Map组合格式 key=url values[field1,field2,field3]
                jsoupPrefix.put(uri, fields);
            }
        }
    }

}