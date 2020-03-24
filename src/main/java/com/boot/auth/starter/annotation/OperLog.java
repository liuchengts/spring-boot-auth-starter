package com.boot.auth.starter.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperLog {
    /**
     * 是否需要日志记录，默认开启
     */
    boolean flag() default true;

    /**
     * 记录类型
     */
    String operType() default "";

}
