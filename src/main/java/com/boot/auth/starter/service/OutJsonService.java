package com.boot.auth.starter.service;

public interface OutJsonService {

    /**
     * 异常输出json
     *
     * @param msg  消息
     * @param code 编码
     * @return 返回json内容
     */
    String errorOutJson(String msg, String code);
}
