package com.soho.spring.configuration;

import com.soho.mybatis.database.selector.DBSelector;
import com.soho.mybatis.database.selector.imp.SimpleDBSelector;
import com.soho.mybatis.interceptor.imp.PageableInterceptor;
import com.soho.spring.datasource.RoutingDataSource;
import com.soho.spring.model.DbConfig;
import com.soho.spring.model.HikariDS;
import com.soho.spring.shiro.initialize.WebInitializeService;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shadow
 */
@Configuration
public class DataSourceConfiguration {

    public final static String MARSTER_DB = "MARSTER_DB";

    @Autowired(required = false)
    private DbConfig dbConfig;
    @Autowired(required = false)
    private WebInitializeService webInitializeService;

    @Bean(name = "dynamicDataSource")
    public DataSource initDynamicDataSource() {
        RoutingDataSource dynamicDataSource = new RoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(4);
        dataSourceMap.put(MARSTER_DB, new HikariDS(MARSTER_DB, dbConfig.getDriverClassName(), dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword()).done());
        List<HikariDS> hikariDSList = webInitializeService.initOtherDataSource();
        if (hikariDSList != null && !hikariDSList.isEmpty()) {
            for (HikariDS ds : hikariDSList) {
                if (MARSTER_DB.equals(ds.getDsName())) {
                    continue;
                }
                dataSourceMap.put(ds.getDsName(), ds.done());
            }
        }
        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(dataSourceMap.get(MARSTER_DB));
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