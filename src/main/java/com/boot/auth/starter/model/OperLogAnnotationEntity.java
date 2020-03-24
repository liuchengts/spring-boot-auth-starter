package com.boot.auth.starter.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperLogAnnotationEntity implements Serializable {
    /**
     * 记录类型
     */
    String operType;
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
    /**
     * ip
     */
    String ip;
    /**
     * 用户编号
     */
    String userNo;
    /**
     * 用户名称
     */
    String username;
    /**
     * 权限
     */
    String roles;
    /**
     * session缺省扩展字段
     */
    Object obj;
}
