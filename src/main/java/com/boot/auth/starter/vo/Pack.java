package com.boot.auth.starter.vo;

import java.util.List;

/**
 * 把成功和失败的相关信息抽象出来，便于与客户端交互
 */
public interface Pack {

    String getSuccess();

    void setSuccess(String success);

    List<ClientMessage> getMessages();

    void setMessages(List<ClientMessage> messages);
}
