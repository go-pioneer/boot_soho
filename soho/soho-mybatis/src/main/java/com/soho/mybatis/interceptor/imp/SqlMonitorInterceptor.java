package com.soho.mybatis.interceptor.imp;

import com.soho.mybatis.database.aconst.DBType;
import com.soho.mybatis.database.selector.DBSelector;
import com.soho.mybatis.interceptor.MybatisInterceptor;
import com.soho.mybatis.interceptor.handler.SqlMonitorHandler;
import com.soho.mybatis.interceptor.handler.domain.LogObj;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * SQL监控拦截器
 *
 * @author shadow
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SqlMonitorInterceptor extends MybatisInterceptor {

    private DBSelector dbSelector;
    private SqlMonitorHandler handler;
    private Boolean logSql = false;

    public SqlMonitorInterceptor(DBSelector dbSelector, SqlMonitorHandler handler, boolean logSql) {
        this.dbSelector = dbSelector;
        this.handler = handler;
        this.logSql = logSql;
    }

    public Object intercept(Invocation invoker) throws Throwable {
        if (logSql == null || !logSql) {
            return invoker.proceed();
        }
        long beginTime = System.currentTimeMillis();
        Exception exception = null;
        Object retObject = null;
        try {
            retObject = invoker.proceed();
            return retObject;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            LogObj logObj = new LogObj(null, beginTime, endTime, endTime - beginTime, null, retObject, null, exception);
            logObj = getRunSql(invoker, logObj);
            if (handler != null) {
                handler.handle(logObj);
            }
        }
    }

    protected LogObj getRunSql(Invocation invoker, LogObj logObj) {
        Object[] queryArgs = invoker.getArgs();
        MappedStatement ms = (MappedStatement) queryArgs[0];
        Object parameterObject = queryArgs[1];
        String sql = getFilledSql(ms, parameterObject, logObj);
        logObj.setLogId(ms.getId());
        logObj.setLogObject(sql);
        logObj.setReqObject(parameterObject);
        return logObj;
    }

    private String getFilledSql(MappedStatement ms, Object parameterObject, LogObj logObj) {
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 根据IBatis3中配置的SQL和传进来的参数进行处理生成可在pl/sql中运行的SQL
        if (parameterMappings != null) {
            MetaObject metaObject = parameterObject == null ? null : MetaObject.forObject(parameterObject, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                    SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = MetaObject.forObject(value, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                                    SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory()).getValue(
                                    propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    if (value != null) {
                        boolean valueIsString = value instanceof String;
                        if (valueIsString && value != null && value.toString().indexOf("$") != 1) {
                            value = ((String) value).replaceAll("\\$", "\\\\\\$");
                        }
                        if (null != value && value instanceof Date) {
                            value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value);
                            if (DBType.MySQL.toString().toUpperCase().equals(dbSelector.getDbType().toUpperCase())) {
                                value = "str_to_date('" + value + "','%Y-%m-%d %H:%i:%s')";
                            } else {
                                value = "to_date('" + value + "','yyyy-MM-dd HH24:mi:ss')";
                            }
                            sql = sql.replaceFirst("\\?", value.toString());
                        } else {
                            sql = sql.replaceFirst("\\?", valueIsString ? "'" + value + "'" : value.toString());
                        }
                    } else {
                        sql = sql.replaceFirst("\\?", "null");

                    }
                }
            }
        }
        sql = sql.replaceAll("(\r\n|\r|\n|\n\r)", "").replaceAll("\\s+", " ");
        return sql;
    }

}
