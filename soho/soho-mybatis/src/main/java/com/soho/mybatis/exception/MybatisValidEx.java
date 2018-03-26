package com.soho.mybatis.exception;

@SuppressWarnings("serial")
public class MybatisValidEx extends RuntimeException {

	public MybatisValidEx() {
		super();
	}

	public MybatisValidEx(String message) {
		super(message);
	}

}
