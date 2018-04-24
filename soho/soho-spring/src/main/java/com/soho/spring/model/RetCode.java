package com.soho.spring.model;

/**
 * @author shadow
 */
public class RetCode {

    public static final String OK_STATUS = "000000";
    public static final String OK_MESSAGE = "操作成功";

    public static final String SESSION_NOTEXIST_STATUS = "999995";
    public static final String SESSION_NOTEXIST_MESSAGE = "您尚未登录或会话已超时";

    public static final String SESSION_KICKOUT_STATUS = "999996";
    public static final String SESSION_KICKOUT_MESSAGE = "您的会话已被踢下线";

    public static final String UNAUTHORIZED_STATUS = "999997";
    public static final String UNAUTHORIZED_MESSAGE = "您没有足够的权限访问";

    public static final String UPLOAD_ERROR_STATUS = "999998";
    public static final String UPLOAD_ERROR_MESSAGE = "上传失败,文件大小超出范围";

    public static final String UNKNOWN_STATUS = "999999";
    public static final String UNKNOWN_MESSAGE = "操作失败";

}
