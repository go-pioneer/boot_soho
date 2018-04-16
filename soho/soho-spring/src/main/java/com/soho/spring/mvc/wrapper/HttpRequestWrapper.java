package com.soho.spring.mvc.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private Map<String, String[]> params = new HashMap<>();

    public HttpRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        String[] values = params.get(name);
        if (values == null || values.length == 0) {
            return null;
        }
        return values[0];
    }

    public String[] getParameterValues(String name) {//同上
        return params.get(name);
    }

    public HttpRequestWrapper addParameter(String name, Object value) {
        if (name != null && value != null) {
            if (value instanceof String[]) {
                params.put(name, (String[]) value);
            } else if (value instanceof String) {
                params.put(name, new String[]{value.toString()});
            } else {
                params.put(name, new String[]{String.valueOf(value)});
            }
        }
        return this;
    }

}