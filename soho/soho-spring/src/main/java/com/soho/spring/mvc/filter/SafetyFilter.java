package com.soho.spring.mvc.filter;

import com.soho.spring.mvc.wrapper.HttpRequestWrapper;
import com.soho.spring.utils.JsoupUtils;
import com.soho.spring.utils.WCCUtils;
import com.soho.spring.utils.XSSUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
public class SafetyFilter implements Filter {

    private final static int max_parameter_size = 50; // 最大参数数量
    private final static int max_field_len = 25; // 最大参数名长度
    private final static int max_querystring_len = 1000; // 最大GET参数名长度
    private final static int max_value_len = 25000; // 最大参数值长度
    private Map<String, String[]> jsoupPrefix = new HashMap<>(); // 富文本过滤,指定请求方法路径,参数名

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
            if (key == null || key == "" || key.length() > max_field_len) {
                continue;
            }
            if (!jsoupPrefix.isEmpty()) {
                for (Map.Entry<String, String[]> entry1 : jsoupPrefix.entrySet()) {
                    if (WCCUtils.test(entry1.getKey(), uri)) {
                        for (String field : entry1.getValue()) {
                            if (key.equals(field)) {
                                isJsoup = true;
                            }
                        }
                    }
                }
            }
            for (String value : values) {
                if (value != null && value.length() > max_value_len) {
                    value = value.substring(0, max_value_len);
                }
                if (isJsoup) {
                    wrapper.addParameter(key, JsoupUtils.safety(value));
                } else {
                    wrapper.addParameter(key, XSSUtils.strip(value));
                }
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        String prefix = config.getInitParameter("jsoupPrefix"); // 拦截数据格式 pattern|field1|field2|field3
        prefix = prefix == null ? "" : prefix;
        String[] prefixs = prefix.split(",");
        for (String str : prefixs) {
            String[] array = str.split("\\|");
            if (array.length < 1) {
                continue;
            }
            String uri = null;
            String[] fields = new String[array.length - 1];
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    uri = array[i];
                } else {
                    fields[i - 1] = array[i];
                }
            }
            if (uri != null && !jsoupPrefix.containsKey(uri)) { // Map组合格式 key=pattern values[field1,field2,field3]
                jsoupPrefix.put(uri, fields);
            }
        }
    }

}