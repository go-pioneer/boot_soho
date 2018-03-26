package com.soho.mybatis.database.aconst;

/**
 * 数据库类型枚举
 * 
 * @author shadow
 * 
 */
public enum DBType {

	DB2("DB2"), Derby("Derby"), HSQL("HSQL"), MySQL("MySQL"), Oracel("Oracel"), PostgreSQL("PostgreSQL"), SQLServer2005("SQLServer2005"), SQLServer(
			"SQLServer"), Sybase(" Sybase");

	private DBType(String value) {
		this.value = value;
	}

	private String value;

	public String toString() {
		return value.toUpperCase();
	}

}
