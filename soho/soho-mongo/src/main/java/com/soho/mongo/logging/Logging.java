package com.soho.mongo.logging;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logging {

    // 是否记录到mongodb
    boolean mongo() default false;

    // 日志表mongodb collection name
    String collection() default "mg_logging";

    // 所属模块名
    String module();

    // 需要删除的mapkey
    String[] delkeys() default {"mqdata"};

}