package com.boot.auth.starter.vo;

import java.io.Serializable;
import java.util.*;

public class PackCommonVo<T extends Serializable> implements PackVo<T>, Pack {

    /**
     * 操作成功与否 由后台返回前台
     */
    private String success = Boolean.TRUE.toString();

    /**
     * 后台处理完成以后的一些消息信息，比如错误信息
     */
    private List<ClientMessage> messages;

    private T vo;

    private Map<String, Object> attribute;

    private List<T> voList = new ArrayList<>();

    @Override
    public String getSuccess() {
        return success;
    }

    @Override
    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ClientMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ClientMessage> messages) {
        this.messages = messages;
    }

    public void addMessages(List<ClientMessage> messages) {
        if (this.messages == null) {
            this.messages = new LinkedList<>();
        }
        this.messages.addAll(messages);
    }

    public void addMessage(ClientMessage message) {
        if (this.messages == null) {
            this.messages = new LinkedList<>();
        }
        this.messages.add(message);
    }

    public T getVo() {
        return vo;
    }

    public void setVo(T vo) {
        this.vo = vo;
    }

    public Map<String, Object> getAttribute() {
        return attribute;
    }

    public void setAttribute(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    public Map<String, Object> addAttribute(String attributeName, Object attributeValue) {
        if (this.attribute == null) {
            this.attribute = new LinkedHashMap<>();
        }
        if (attributeName == null) {
            throw new IllegalArgumentException("Model attribute name must not be null");
        }
        this.attribute.put(attributeName, attributeValue);
        return this.attribute;
    }

    public List<T> getVoList() {
        return voList;
    }

    public void setVoList(List<T> voList) {
        this.voList = voList;
    }
}
