package com.soho.mybatis.exception;

/**
 * 
 * @author shadow
 * 
 */
@SuppressWarnings("serial")
public class MybatisDAOEx extends Exception {

	private String errorCode;

	public MybatisDAOEx() {
		super();
	}

	public MybatisDAOEx(Throwable e) {
		super(e);
	}

	public MybatisDAOEx(String msg) {
		super(msg);
	}

	public MybatisDAOEx(String msg, Throwable e) {
		super(msg, e);
	}

	public MybatisDAOEx(String errorCode, String msg) {
		super(msg);
		setErrorCode(errorCode);
	}

	public MybatisDAOEx(String msg, Throwable e, String errorCode) {
		super(msg, e);
		setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
