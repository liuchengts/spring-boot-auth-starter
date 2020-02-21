package com.boot.auth.starter.vo;

/**
 * 与客户端交互时，返回的信息
 *
 */
public interface AMessage {

    /**
     * 返回处理成功与否
     * @return "true" or "false"
     */
    String getSuccess();

    /**
     * 设置成功与否
     *
     * @param success 成功与否
     */
    void setSuccess(String success);

    /**
     * 返回信息的统一代码
     *
     * @return code 统一代码
     */
    String getCode();

    /**
     * 设置信息的统一代码
     *
     * @param code 信息的统一代码
     */
    void setCode(String code);

    /**
     * 返回具体处理信息
     *
     * @return 具体处理信息
     */
    String getMessage();

    /**
     * 设置具体信息
     *
     * @param message 具体处理信息
     */
    void setMessage(String message);
}
