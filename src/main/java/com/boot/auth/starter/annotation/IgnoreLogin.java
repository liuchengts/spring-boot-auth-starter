package com.boot.auth.starter.annotation;

import java.lang.annotation.*;

/**
 * 允许不登录
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface IgnoreLogin {
    boolean ignore() default true;
}

