package com.boot.auth.starter.annotation;

import java.lang.annotation.*;

/**
 * 不强制权限校验
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthGetSession {
    /**
     * 是否必须登录
     * 如果为false  则不会进行登录验证，也不会注入 Session 缓存当前用户信息
     * @return loginRequired
     */
    boolean loginRequired() default true;
}
