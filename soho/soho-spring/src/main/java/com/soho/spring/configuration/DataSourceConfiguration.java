package com.soho.spring.configuration;

import com.soho.mybatis.database.selector.DBSelector;
import com.soho.mybatis.database.selector.imp.SimpleDBSelector;
import com.soho.mybatis.interceptor.imp.PageableInterceptor;
import com.soho.spring.datasource.RoutingDataSource;
import com.soho.spring.model.DBConfig;
import com.soho.spring.model.DeftConfig;
import com.soho.spring.model.HikariDS;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @author shadow
 */
@Configuration
public class DataSourceConfiguration {

    @Bean
    public RoutingDataSource routingDataSource(DBConfig config) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        DataSource hikariDS = new HikariDS(config.getDriverClassName(), config.getUrl(), config.getUsername(), config.getPassword()).done();
        routingDataSource.setDefaultTargetDataSource(hikariDS);
        routingDataSource.setTargetDataSources(new HashMap<>());
        return routingDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(RoutingDataSource dataSource, DBSelector dbSelector, DeftConfig config) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        Resource[] mappers = new PathMatchingResourcePatternResolver().getResources(config.getMgbXmlLocation());
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

    @Bean
    public DBSelector dbSelector(DBConfig config) {
        return new SimpleDBSelector(config.getDatabase());
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager(RoutingDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}