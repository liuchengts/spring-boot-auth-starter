package com.boot.auth.starter.model;

import java.io.Serializable;

public class OperLogAnnotationEntity implements Serializable {
    /**
     * 时间戳
     */
    Long time = System.currentTimeMillis();
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

    public Long getTime() {
        return time;
    }

    public String getOperType() {
        return operType;
    }

    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
