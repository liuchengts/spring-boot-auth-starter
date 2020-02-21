package com.boot.auth.starter.common;

/**
 * restful 接口状态码
 */
public enum RestStatus {

    /*交互成功返回码：1000*/
    OK(1000, "成功"),

    /*系统错误：9001-9999，泛指失败返回码，无法具体定义出错误码/框架层/拦截层使用*/
    SYSTEM_ERROR(9999, "系统异常"),// 后端代码异常错误码，为方便调试，这个错误信息前端/APP端不会提示给用户
    /**
     * 需要友好提示给用户的信息（最好是产品确定的文案，可以先开发自定），有跳转需求的自定义码需要单独定义，
     * 注意点：1、小程序端/APP端针对这类提示必须在具体每个页面里面去处理，如果需要提示但没有给前端小伙伴说明，不予处理（没有记住的后端的锅）
     */
    SYSTEM_USER_TIPS(9000, "用户友好提示信息"),

    /*参数类错误：1001-1999*/
    PARAM_INVALID(1001, "参数无效"),
    PARAM_EMPTY(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_DELETION(1004, "参数缺失"),

    /*用户类错误：2001-2999*/
    USER_NOLOGIIN(2001, "用户未登录"),
    USER_DISABLED(2002, "用户不可用"),
    USER_TOKEN_INVALID(2003, "用户token失效"),
    USER_CODE_INVALID(2004, "验证码无效"),

    /*三方类错误：4001-4999*/
    THREE_ERROR(4001, "系统繁忙，请稍后重试"),

    /*服务类错误：5001-5999*/
    SRV_EXCP_INT(5001, "内部服务调用异常"),
    SRV_EXCP_EXT(5002, "外部服务调用异常"),

    /*流控类错误：6001-6999*/
    API_NOACCESS(6001, "该接口禁止访问"),
    API_ADDR_INVALID(6002, "接口地址无效"),
    API_TIMEOUT(6003, "接口请求超时"),
    API_LOAD_UP(6004, "接口负载过高"),

    /*权限类错误：7001-7999 (常用于对外服务API)*/
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
