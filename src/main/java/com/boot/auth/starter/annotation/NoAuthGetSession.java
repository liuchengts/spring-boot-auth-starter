package com.boot.auth.starter.annotation;

import java.lang.annotation.*;

/**
 * 不强制权限校验
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthGetSession {
    boolean loginRequired() default true;
}
