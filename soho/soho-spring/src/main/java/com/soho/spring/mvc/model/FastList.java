package com.soho.spring.mvc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
public class FastList<T> {

    private List<T> list;

    public FastList() {
        list = new ArrayList<>();
    }

    public FastList(int len) {
        list = new ArrayList<>(len);
    }

    public FastList add(T value) {
        list.add(value);
        return this;
    }

    public FastList addAll(List<T> values) {
        list.addAll(values);
        return this;
    }

    public List<T> done() {
        return list;
    }

}
