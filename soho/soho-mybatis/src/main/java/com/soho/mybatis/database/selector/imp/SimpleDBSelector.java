package com.soho.mybatis.database.selector.imp;

import com.soho.mybatis.database.aconst.DBType;
import com.soho.mybatis.database.dialect.Dialect;
import com.soho.mybatis.database.dialect.imp.DB2Dialect;
import com.soho.mybatis.database.dialect.imp.DerbyDialect;
import com.soho.mybatis.database.dialect.imp.HSQLDialect;
import com.soho.mybatis.database.dialect.imp.MySQLDialect;
import com.soho.mybatis.database.dialect.imp.OracleDialect;
import com.soho.mybatis.database.dialect.imp.PostgreSQLDialect;
import com.soho.mybatis.database.dialect.imp.SQLServer2005Dialect;
import com.soho.mybatis.database.dialect.imp.SQLServerDialect;
import com.soho.mybatis.database.dialect.imp.SybaseDialect;
import com.soho.mybatis.database.selector.DBSelector;

public class SimpleDBSelector implements DBSelector {

    private String dbType;
    private Dialect dialect;

    public SimpleDBSelector() {

    }

    public SimpleDBSelector(String dbType) {
        this.dbType = dbType;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Dialect getDialect() {
        if (dialect == null) {
            if (dbType == null || "".equals(dbType)) {
                dialect = new MySQLDialect();
            } else {
                dbType = dbType.trim().toUpperCase();
                if (dbType.equals(DBType.DB2.toString())) {
                    dialect = new DB2Dialect();
                } else if (dbType.equals(DBType.Derby.toString())) {
                    dialect = new DerbyDialect();
                } else if (dbType.equals(DBType.HSQL.toString())) {
                    dialect = new HSQLDialect();
                } else if (dbType.equals(DBType.MySQL.toString())) {
                    dialect = new MySQLDialect();
                } else if (dbType.equals(DBType.Oracel.toString())) {
                    dialect = new OracleDialect();
                } else if (dbType.equals(DBType.PostgreSQL.toString())) {
                    dialect = new PostgreSQLDialect();
                } else if (dbType.equals(DBType.SQLServer2005.toString())) {
                    dialect = new SQLServer2005Dialect();
                } else if (dbType.equals(DBType.SQLServer.toString())) {
                    dialect = new SQLServerDialect();
                } else if (dbType.equals(DBType.Sybase.toString())) {
                    dialect = new SybaseDialect();
                } else {
                    dialect = new MySQLDialect();
                }
            }
        }
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

}
