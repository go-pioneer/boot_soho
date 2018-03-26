package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class PostgreSQLDialect implements Dialect {

	public PostgreSQLDialect() {
	}

	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		return (new StringBuffer(sql.length() + 20))
				.append(sql)
				.append(offset <= 0 ? (new StringBuilder()).append(" limit ").append(limitPlaceholder).toString() : (new StringBuilder())
						.append(" limit ").append(limitPlaceholder).append(" offset ").append(offsetPlaceholder).toString()).toString();
	}
	
}