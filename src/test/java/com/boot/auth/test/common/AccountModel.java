package com.boot.auth.test.common;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.test.custom.RolesConstant;

import java.io.Serializable;

/**
 * @author liucheng
 * @version 1.0
 * @description: 账户
 * @date 2023/6/5 17:30
 */
public class AccountModel implements Serializable {
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 用户组
     */
    private String group;
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 用户权限
     */
    private String roles;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

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

    public AccountModel() {
    }

    public AccountModel(String nickName, String group, String userNo, String roles) {
        this.nickName = nickName;
        this.group = group;
        this.userNo = userNo;
        this.roles = roles;
    }

    /**
     * 获取一个默认的用户
     */
    public static AccountModel findAccount() {
        return new AccountModel("我是用户昵称", "我是group", "U123456",
                String.join(AuthConstant.HEAD_TOKEN_SEPARATOR, RolesConstant.USER_1, RolesConstant.DEFAULT));
    }
}
