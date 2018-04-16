package com.soho.spring.mvc.filter;

import com.soho.spring.mvc.wrapper.HttpRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @author shadow
 */
public class SafetyFilter implements Filter {

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain
            filterChain)
            throws IOException, ServletException {
        HttpRequestWrapper wrapper = new HttpRequestWrapper((HttpServletRequest) servletRequest);
        Map<String, String[]> map = servletRequest.getParameterMap();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String[]> entry : map.entrySet()) {
                for (String value : entry.getValue()) {
                    wrapper.addParameter(entry.getKey(), value);
                }
            }
        }
        filterChain.doFilter(wrapper, servletResponse);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}