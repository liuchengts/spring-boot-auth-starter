package com.boot.auth.starter.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 返回给客户端的消息。
 */
public class ClientMessage implements Serializable {

    private String code;

    private String message;

    /**
     * <p>动态替换的信息。</p>
     * <p>key：跟消息里面的 [] 方括号里面的String一致，value：动态替换进出的内容。</p>
     */
    private Map<?, ?> content;

    public ClientMessage() {
    }

    public ClientMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<?, ?> getContent() {
        return content;
    }

    public void setContent(Map<?, ?> content) {
        this.content = content;
    }
}
