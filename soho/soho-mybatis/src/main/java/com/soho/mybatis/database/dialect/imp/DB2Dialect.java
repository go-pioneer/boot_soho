package com.soho.mybatis.database.dialect.imp;

import com.soho.mybatis.database.dialect.Dialect;

public class DB2Dialect implements Dialect {

	public DB2Dialect() {
	}

	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	private static String getRowNumber(String sql) {
		StringBuffer rownumber = (new StringBuffer(50)).append("rownumber() over(");
		int orderByIndex = sql.toLowerCase().indexOf("order by");
		if (orderByIndex > 0 && !hasDistinct(sql))
			rownumber.append(sql.substring(orderByIndex));
		rownumber.append(") as rownumber_,");
		return rownumber.toString();
	}

	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct") >= 0;
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuffer pagingSelect = (new StringBuffer(sql.length() + 100)).append(sql.substring(0, startOfSelect)).append("select * from ( select ")
				.append(getRowNumber(sql));
		if (hasDistinct(sql))
			pagingSelect.append(" row_.* from ( ").append(sql.substring(startOfSelect)).append(" ) as row_");
		else
			pagingSelect.append(sql.substring(startOfSelect + 6));
		pagingSelect.append(" ) as temp_ where rownumber_ ");
		if (offset > 0) {
			String endString = (new StringBuilder()).append(offsetPlaceholder).append("+").append(limitPlaceholder).toString();
			pagingSelect.append((new StringBuilder()).append("between ").append(offsetPlaceholder).append("+1 and ").append(endString).toString());
		} else {
			pagingSelect.append((new StringBuilder()).append("<= ").append(limitPlaceholder).toString());
		}
		return pagingSelect.toString();
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

}