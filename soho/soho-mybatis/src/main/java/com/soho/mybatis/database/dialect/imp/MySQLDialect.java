package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class MySQLDialect implements Dialect {

	public MySQLDialect() {
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	public boolean supportsLimit() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		return (new StringBuilder()).append(sql).append(" limit ").append((offset - 1) * limit).append(",").append(limit).toString();
	}

}