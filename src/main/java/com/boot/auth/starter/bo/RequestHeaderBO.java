package com.boot.auth.starter.bo;

import java.io.Serializable;

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
}
