package com.soho.spring.model;

/**
 * 常用正则表达式
 *
 * @author shadow
 */
public enum RGX {

    // 正整数
    INTEGER("(^[1-9]*[1-9][0-9]*$)|0"),
    // 非负浮点数
    FLOAT("(^[1-9]+(.[0-9]+)?$)|(^[0]+(.[0-9]+)?$)"),
    // 手机格式
    MOBILE("^1[3|4|5|6|7|8|9][0-9]{9}"),
    // 邮箱格式
    EMAIL("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
    // 帐号格式 字母开头+数字或下划线6-15位
    ACCOUNT("^[a-zA-Z][a-zA-Z0-9_]{5,14}$"),
    // 密码格式 任意字符6-18位
    PASSWORD("^.{6,18}?"),
    // URL地址
    URL("^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$");


    private RGX(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }

}
