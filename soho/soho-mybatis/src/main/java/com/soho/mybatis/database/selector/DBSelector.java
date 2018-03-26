package com.soho.mybatis.database.selector;

import com.soho.mybatis.database.dialect.Dialect;

public interface DBSelector {

	public String getDbType();

	public void setDbType(String dbType);

	public Dialect getDialect();

	public void setDialect(Dialect dialect);

}
