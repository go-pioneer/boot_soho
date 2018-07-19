package com.soho.mybatis.sqlcode.condition;

import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.aconst.SortBy;
import com.soho.mybatis.sqlcode.domain.Condition;
import com.soho.mybatis.sqlcode.domain.Join;

import java.util.List;

public interface Cnd {

    // =
    public Cnd eq(String key, Object value);

    // <>
    public Cnd noteq(String key, Object value);

    // <
    public Cnd lt(String key, Object value);

    // <=
    public Cnd lte(String key, Object value);

    // >
    public Cnd gt(String key, Object value);

    // >=
    public Cnd gte(String key, Object value);

    // is null
    public Cnd isnull(String key);

    // is not null
    public Cnd notnull(String key);

    // between x and y
    public Cnd between(String key, Object value1, Object value2);

    // not between x and y
    public Cnd notbetween(String key, Object value1, Object value2);

    // in
    public Cnd in(String key, Object... values);

    // in
    public Cnd in(String key, Object values);

    // not in
    public Cnd notin(String key, Object... values);

    // not in
    public Cnd notin(String key, Object values);

    // like
    public Cnd like(String key, Object value);

    // not like
    public Cnd notlike(String key, Object value);

    // or
    public Cnd or(Cnd... cnds);

    // 分页方法(根据数据库方言)
    public Cnd limit(Integer pageNo, Integer pageSize);

    // 分页方法(根据数据库方言)
    public Cnd limit(Integer pageNo, Integer pageSize, boolean spilled);

    // 分页方法(根据数据库方言)
    public Cnd limit(Integer pageSize);

    // distinct
    public Cnd distinct(String... keys);

    // group by
    public Cnd groupby(String... keys);

    // order by
    public Cnd orderby(String key, SortBy sortBy);

    // 获取指定字段
    public Cnd fields(String... keys);

    // 指定更新字段
    public Cnd addUpdateKeyValue(String[] keys, Object... values);

    // 指定更新对象(自定义sql时使用)
    public Cnd addUpdateObj(Object obj);

    // 传递额外参数(自定义sql时使用)
    public Cnd addOther(String key, Object value);

    // from table1 a(连表查询使用)
    public Cnd from(String table);

    // left(right,inner) join table1 b(连表查询使用)
    public Cnd join(Join join);

    // 复制条件
    public Cnd copy(Cnd cnd);

    public List<Condition<?>> getConditions();

    public Pagination<Object> getPagination();

    public void setPagination(Pagination<Object> pagination);

}
