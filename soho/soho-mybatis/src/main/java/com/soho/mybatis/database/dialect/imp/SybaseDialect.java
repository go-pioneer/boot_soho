package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class SybaseDialect implements Dialect {

	public SybaseDialect() {
	}

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return false;
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		throw new UnsupportedOperationException("paged queries not supported");
	}

}
