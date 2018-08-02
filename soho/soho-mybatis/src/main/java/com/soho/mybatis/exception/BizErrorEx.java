package com.soho.mybatis.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author shadow
 */
@SuppressWarnings("serial")
public class BizErrorEx extends Exception {

    private static final Logger log = LoggerFactory.getLogger(BizErrorEx.class);

    private String errorCode;
    private String msg;
    private Object errorObject;
    private HttpStatus httpStatus;

    public BizErrorEx() {
        this(null, null, null, null, HttpStatus.OK);
    }

    public BizErrorEx(Throwable e) {
        this(e, null, null, null, HttpStatus.OK);
    }

    public BizErrorEx(String msg) {
        this(null, "999100", msg, null, HttpStatus.OK);
    }

    public BizErrorEx(String msg, Throwable e) {
        this(e, null, msg, null, HttpStatus.OK);
    }

    public BizErrorEx(String errorCode, String msg) {
        this(null, errorCode, msg, null, HttpStatus.OK);
    }

    public BizErrorEx(String errorCode, String msg, HttpStatus httpStatus) {
        this(null, errorCode, msg, null, httpStatus);
    }

    public BizErrorEx(String errorCode, String msg, Object errorObject, HttpStatus httpStatus) {
        this(null, errorCode, msg, errorObject, httpStatus);
    }

    public BizErrorEx(String errorCode, String msg, Throwable e) {
        this(e, errorCode, msg, null, HttpStatus.OK);
    }

    public BizErrorEx(Throwable e, String errorCode, String msg, Object errorObject, HttpStatus httpStatus) {
        super(msg, e);
        this.errorCode = errorCode;
        this.msg = msg;
        this.errorObject = errorObject;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(Object errorObject) {
        this.errorObject = errorObject;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public static BizErrorEx transform(Exception e) throws BizErrorEx {
        if (log.isDebugEnabled()) {
            log.error(e.getMessage(), e);
        }
        if (e instanceof MybatisDAOEx) {
            throw new BizErrorEx(((MybatisDAOEx) e).getErrorCode(), e.getMessage());
        } else if (e instanceof BizErrorEx) {
            throw (BizErrorEx) e;
        } else if (e instanceof Exception) {
            throw new BizErrorEx("999100", "业务系统繁忙");
        } else {
            return null;
        }
    }

}
