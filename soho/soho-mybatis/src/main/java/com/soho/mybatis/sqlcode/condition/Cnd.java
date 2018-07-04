package com.soho.mybatis.sqlcode.condition;

import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.aconst.SortBy;
import com.soho.mybatis.sqlcode.domain.Condition;
import com.soho.mybatis.sqlcode.domain.Join;

import java.util.List;

public interface Cnd {

    public Cnd eq(String key, Object value);

    public Cnd noteq(String key, Object value);

    public Cnd lt(String key, Object value);

    public Cnd lte(String key, Object value);

    public Cnd gt(String key, Object value);

    public Cnd gte(String key, Object value);

    public Cnd isnull(String key);

    public Cnd notnull(String key);

    public Cnd between(String key, Object value1, Object value2);

    public Cnd notbetween(String key, Object value1, Object value2);

    public Cnd in(String key, Object... values);

    public Cnd in(String key, Object values);

    public Cnd notin(String key, Object... values);

    public Cnd notin(String key, Object values);

    public Cnd like(String key, Object value);

    public Cnd notlike(String key, Object value);

    public Cnd or(Cnd... cnds);

    public Cnd limit(Integer pageNo, Integer pageSize);

    public Cnd limit(Integer pageNo, Integer pageSize, boolean spilled);

    public Cnd limit(Integer pageSize);

    public Cnd distinct(String... keys);

    public Cnd groupby(String... keys);

    public Cnd orderby(String key, SortBy sortBy);

    public Cnd fields(String... keys);

    public Cnd addUpdateObj(Object obj);

    public Cnd addUpdateKeyValue(String[] keys, Object... values);

    public Cnd addOther(String key, Object value);

    public List<Condition<?>> getConditions();

    public Pagination<Object> getPagination();

    public void setPagination(Pagination<Object> pagination);

    public Cnd from(String table);

    public Cnd join(Join join);

    public Cnd copy(Cnd cnd);

}
