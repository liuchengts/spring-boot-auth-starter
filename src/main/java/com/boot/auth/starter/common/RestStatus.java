package com.boot.auth.starter.common;

/**
 * restful 接口状态码
 */
public enum RestStatus {

    /*系统错误：*/
    SYSTEM_ERROR(9999, "系统异常"),
    SYSTEM_CACHE_ERROR(9998, "系统缓存初始化异常"),
    SYSTEM_CACHE_KEY_ERROR(9997, "系统缓存key解析异常"),
    /*用户类错误：2001-2999*/
    USER_NOLOGIIN(2001, "用户未登录"),
    USER_DISABLED(2002, "用户不可用"),
    USER_TOKEN_INVALID(2003, "用户token失效"),
    USER_CODE_INVALID(2004, "验证码无效"),
    /*权限类错误：7001-7999*/
    AUTH_NO(7001, "无权访问");

    private final int code;
    private final String msg;

    RestStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int value() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
