package com.soho.spring.model;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * Hikari高性能数据源初始化实现类
 * 配置参数通过大量项目实践取舍平衡,建议无需修改
 * 示例: new HikariDS("DB_MARSTER", "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/test_db", "root", "123456").done()
 * created by shadow on 2018.5.17
 */
public class HikariDS {

    private String dsName;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Integer maximumPoolSize = 20;
    private Integer minimumIdle = 10;
    private Integer connectionTimeout = 30000;
    private Integer idleTimeout = 600000;
    private Integer maxLifetime = 1800000;

    public HikariDS() {

    }

    public HikariDS(String driverClassName, String url, String username, String password) {
        this(null, driverClassName, url, username, password);
    }

    public HikariDS(String dsName, String driverClassName, String url, String username, String password) {
        this.dsName = dsName;
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public String getDsName() {
        return dsName;
    }

    public HikariDS setDsName(String dsName) {
        this.dsName = dsName;
        return this;
    }

    public HikariDS setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
        return this;
    }

    public HikariDS setUrl(String url) {
        this.url = url;
        return this;
    }

    public HikariDS setUsername(String username) {
        this.username = username;
        return this;
    }

    public HikariDS setPassword(String password) {
        this.password = password;
        return this;
    }

    public HikariDS setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public HikariDS setMinimumIdle(Integer minimumIdle) {
        this.minimumIdle = minimumIdle;
        return this;
    }

    public HikariDS setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public HikariDS setIdleTimeout(Integer idleTimeout) {
        this.idleTimeout = idleTimeout;
        return this;
    }

    public HikariDS setMaxLifetime(Integer maxLifetime) {
        this.maxLifetime = maxLifetime;
        return this;
    }

    public DataSource done() {
        HikariDataSource hikariDataSource =
                DataSourceBuilder.create()
                        .type(HikariDataSource.class)
                        .driverClassName(driverClassName)
                        .url(url)
                        .username(username)
                        .password(password)
                        .build();
        hikariDataSource.setMaximumPoolSize(maximumPoolSize);
        hikariDataSource.setMinimumIdle(minimumIdle);
        hikariDataSource.setConnectionTimeout(connectionTimeout);
        hikariDataSource.setIdleTimeout(idleTimeout);
        hikariDataSource.setMaxLifetime(maxLifetime);
        return hikariDataSource;
    }

}
