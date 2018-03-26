package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class SQLServerDialect implements Dialect {

	public SQLServerDialect() {
	}

	public boolean supportsLimitOffset() {
		return false;
	}

	public boolean supportsLimit() {
		return true;
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex != selectIndex ? 6 : 15);
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, null, limit, null);
	}

	public String getLimitString(String querySelect, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		if (offset > 0)
			throw new UnsupportedOperationException("sql server has no offset");
		else
			return (new StringBuffer(querySelect.length() + 8)).append(querySelect)
					.insert(getAfterSelectInsertPoint(querySelect), (new StringBuilder()).append(" top ").append(limit).toString()).toString();
	}
	
}