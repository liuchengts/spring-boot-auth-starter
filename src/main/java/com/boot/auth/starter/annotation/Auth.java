package com.boot.auth.starter.annotation;

import com.boot.auth.starter.common.DefaultRolesConstant;

import java.lang.annotation.*;

/**
 * 权限校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface Auth {
    /**
     * 需要的角色
     *
     * @return roles
     */
    String[] roles() default {DefaultRolesConstant.DEFAULT};
}
