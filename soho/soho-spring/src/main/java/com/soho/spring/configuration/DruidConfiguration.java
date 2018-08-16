package com.soho.spring.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.soho.mybatis.database.selector.DBSelector;
import com.soho.mybatis.database.selector.imp.SimpleDBSelector;
import com.soho.mybatis.interceptor.imp.PageableInterceptor;
import com.soho.spring.datasource.DataSourceKey;
import com.soho.spring.datasource.DynamicRoutingDataSource;
import com.soho.spring.model.DbConfig;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shadow
 */
@Configuration
public class DruidConfiguration {

    @Autowired(required = false)
    private DbConfig dbConfig;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", dbConfig.getUsername());
        reg.addInitParameter("loginPassword", dbConfig.getPassword());
        reg.addInitParameter("logSlowSql", dbConfig.getLogSlowSql());
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    public DataSource db_master(DbConfig dbConfig) {
        /*return DataSourceBuilder.create()
                .type(DruidDataSource.class)
                .driverClassName(dbConfig.getDriverClassName())
                .username(dbConfig.getUsername())
                .password(dbConfig.getPassword())
                .build();*/
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbConfig.getUrl());
        datasource.setUsername(dbConfig.getUsername());
        datasource.setPassword(dbConfig.getPassword());
        datasource.setDriverClassName(dbConfig.getDriverClassName());
        datasource.setInitialSize(dbConfig.getInitialSize());
        datasource.setMinIdle(dbConfig.getMinIdle());
        datasource.setMaxActive(dbConfig.getMaxActive());
        datasource.setMaxWait(dbConfig.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dbConfig.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dbConfig.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dbConfig.getValidationQuery());
        datasource.setTestWhileIdle(dbConfig.isTestWhileIdle());
        datasource.setTestOnBorrow(dbConfig.isTestOnBorrow());
        datasource.setTestOnReturn(dbConfig.isTestOnReturn());
        try {
            datasource.setFilters(dbConfig.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    public DataSource db_slave(DbConfig dbConfig) {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(dbConfig.getUrl());
        datasource.setUsername(dbConfig.getUsername());
        datasource.setPassword(dbConfig.getPassword());
        datasource.setDriverClassName(dbConfig.getDriverClassName());
        datasource.setInitialSize(dbConfig.getInitialSize());
        datasource.setMinIdle(dbConfig.getMinIdle());
        datasource.setMaxActive(dbConfig.getMaxActive());
        datasource.setMaxWait(dbConfig.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(dbConfig.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(dbConfig.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(dbConfig.getValidationQuery());
        datasource.setTestWhileIdle(dbConfig.isTestWhileIdle());
        datasource.setTestOnBorrow(dbConfig.isTestOnBorrow());
        datasource.setTestOnReturn(dbConfig.isTestOnReturn());
        try {
            datasource.setFilters(dbConfig.getFilters());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    @Bean(name = "dynamicDataSource")
    public DataSource initDynamicDataSource() {
        DynamicRoutingDataSource dynamicDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(DataSourceKey.DB_MASTER, db_master(dbConfig));
        dataSourceMap.put(DataSourceKey.DB_SLAVE, db_slave(dbConfig));
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.get(DataSourceKey.DB_MASTER));
        return dynamicDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource, @Qualifier("dbSelector") DBSelector dbSelector) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] mappers = new PathMatchingResourcePatternResolver().getResources(dbConfig.getMgbXmlLocation());
        Resource sql = new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis-sqlresolver.xml")[0];
        int len = mappers.length + 1;
        Resource[] resources = new Resource[len];
        for (int i = 0; i < len; i++) {
            if (len - 1 == i) {
                resources[i] = sql;
            } else {
                resources[i] = mappers[i];
            }
        }
        bean.setMapperLocations(resources);
        bean.setPlugins(new Interceptor[]{new PageableInterceptor(dbSelector)});
        return bean.getObject();
    }

    @Bean(name = "dbSelector")
    public DBSelector dbSelector() {
        return new SimpleDBSelector(dbConfig.getDatabase());
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dynamicDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}