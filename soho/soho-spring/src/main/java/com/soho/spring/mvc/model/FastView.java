package com.soho.spring.mvc.model;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author shadow
 */
public class FastView {

    private ModelAndView view;

    public FastView() {
        view = new ModelAndView();
    }

    public FastView(String path) {
        this();
        view.setViewName(path);
    }

    public FastView add(String key, Object value) {
        view.addObject(key, value);
        return this;
    }

    public FastView addAll(Map<String, Object> map) {
        view.addAllObjects(map);
        return this;
    }

    public ModelAndView done() {
        return view;
    }

}
