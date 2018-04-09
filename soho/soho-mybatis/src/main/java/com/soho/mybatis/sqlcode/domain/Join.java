package com.soho.mybatis.sqlcode.domain;

import com.soho.mybatis.crud.aconst.MODE;
import com.soho.mybatis.sqlcode.aconst.OPT;

import java.util.ArrayList;
import java.util.List;

public class Join {

    private MODE mode;
    private String table;
    private List<Condition<?>> conditions = new ArrayList<>();

    public Join() {

    }

    public Join(MODE mode, String table) {
        this.mode = mode;
        this.table = table;
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<Condition<?>> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition<?>> conditions) {
        this.conditions = conditions;
    }

    public Join on(String key, Object value) {
        return on(OPT.EQ, key, value);
    }

    public Join on(OPT opt, String key, Object value) {
        conditions.add(new Condition<>(opt, key, value));
        return this;
    }

}
