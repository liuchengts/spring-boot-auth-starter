package com.boot.auth.starter.common;

public class Session {
    /**
     * 用户编号-必须
     */
    public String userNo;
    /**
     * 权限角色--必须
     */
    public String roles;
    /**
     * 访问ip
     */
    public String ip;
    /**
     * 登录平台
     */
    public String platform;
    /**
     * 版本
     */
    public String version;
    /**
     * 微信unionId
     */
    public String unionId;
    /**
     * 用户名称
     */
    public String username;
    /**
     * 微信openId
     */
    public String openId;
    /**
     * 头像
     */
    public String avatar;
    /**
     * 手机号
     */
    public String mobile;
    /**
     * 扩展字段
     */
    public Object obj;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
