package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class HSQLDialect implements Dialect {

	public HSQLDialect() {
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
		boolean hasOffset = offset > 0;
		return (new StringBuffer(sql.length() + 10))
				.append(sql)
				.insert(sql.toLowerCase().indexOf("select") + 6,
						hasOffset ? (new StringBuilder()).append(" limit ").append(offsetPlaceholder).append(" ").append(limitPlaceholder).toString()
								: (new StringBuilder()).append(" top ").append(limitPlaceholder).toString()).toString();
	}
	
}