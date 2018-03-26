package com.soho.mybatis.sqlcode.aconst;

/**
 * 排序定值枚举
 * 
 * @author shadow
 * 
 */
public enum SortBy {

	A("ASC"), D("DESC");

	private SortBy(String value) {
		this.value = value;
	}

	private String value;

	public String toString() {
		return value;
	}

}
