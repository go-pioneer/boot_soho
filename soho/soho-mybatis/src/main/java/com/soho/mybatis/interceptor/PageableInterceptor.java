package com.soho.mybatis.interceptor;

import com.soho.mybatis.database.selector.DBSelector;
import com.soho.mybatis.pageable.Pagination;
import com.soho.mybatis.sqlcode.condition.Cnd;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * 分页拦截器
 *
 * @author shadow
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageableInterceptor extends MybatisInterceptor {

    private DBSelector dbSelector;

    public DBSelector getDbSelector() {
        return dbSelector;
    }

    public void setDbSelector(DBSelector dbSelector) {
        this.dbSelector = dbSelector;
    }


    /* (non-Javadoc)
     * 拦截器要执行的方法
     */
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        // MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        BoundSql boundSql = statementHandler.getBoundSql();
        Object obj = boundSql.getParameterObject();
        if (obj instanceof Cnd) {
            Pagination<?> pagination = ((Cnd) obj).getPagination();
            if (pagination != null && pagination.getPageNo() != null && pagination.getPageNo().intValue() > 0) {
                String sql = boundSql.getSql();
                String countSql = getCountSql(sql);
                Connection connection = (Connection) invocation.getArgs()[0];
                PreparedStatement countStatement = connection.prepareStatement(countSql);
                ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
                parameterHandler.setParameters(countStatement);
                ResultSet rs = countStatement.executeQuery();
                if (rs.next()) {
                    pagination.setPageTotal(rs.getInt(1));
                }
                fillPagination(pagination);
                String pageSql = getPageSql(pagination, sql);
                metaObject.setValue("delegate.boundSql.sql", pageSql);
            }
        }
        return invocation.proceed();
    }

    private void fillPagination(Pagination<?> pagination) {
        Integer pageNo = pagination.getPageNo();
        Integer pageSize = pagination.getPageSize();
        Integer pageTotal = pagination.getPageTotal();
        Integer pageNumber = 0;
        if (pageSize == null || pageSize <= 0 || pageSize > 1000) {
            pageSize = 10;
        }
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        if (pageTotal == null || pageTotal < 0) {
            pageTotal = 0;
        }
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setPageTotal(pageTotal);
        pageNumber = (pageTotal % pageSize == 0) ? (pageTotal / pageSize) : (pageTotal / pageSize + 1);
        pagination.setPageNumber(pageNumber);
    }

    private String getCountSql(String sql) {
        return "select count(1) from (" + sql + ") as x";
    }

    private String getPageSql(Pagination<?> page, String sql) {
        return dbSelector.getDialect().getLimitString(sql, page.getPageNo(), page.getPageSize());
    }

    /* (non-Javadoc)
     * 拦截器需要拦截的对象
     */
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /* (non-Javadoc)
     * 设置初始化的属性值
     */
    public void setProperties(Properties properties) {

    }


}