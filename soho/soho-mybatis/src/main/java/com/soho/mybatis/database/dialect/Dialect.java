package com.soho.mybatis.database.dialect;

public interface Dialect {

	public boolean supportsLimit();

	public boolean supportsLimitOffset();

	public String getLimitString(String sql, int offset, int limit);

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder);

}