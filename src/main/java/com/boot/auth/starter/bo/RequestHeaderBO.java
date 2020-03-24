package com.boot.auth.starter.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RequestHeaderBO implements Serializable {
    /**
     * 平台
     */
    String platform;
    /**
     * 渠道
     */
    String channel;
    /**
     * 版本
     */
    String version;
    /**
     * 设备id
     */
    String deviceId;
}
