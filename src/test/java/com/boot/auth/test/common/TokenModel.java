package com.boot.auth.test.common;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.test.custom.RolesConstant;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author liucheng
 * @version 1.0
 * @description: 账户
 * @date 2023/6/5 17:30
 */
public class TokenModel implements Serializable {
    /**
     * key
     */
    private String key;
    /**
     * 用户组
     */
    private String value;
    /**
     * 创建时间
     */
    private LocalDateTime createAt;
    /**
     * 修改时间
     */
    private LocalDateTime updateAt;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public TokenModel() {
    }

    public TokenModel(String key, String value, LocalDateTime createAt, LocalDateTime updateAt) {
        this.key = key;
        this.value = value;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    /**
     * 获取一个启用了排他功能的token
     */
    public static TokenModel findEnableExcludeToken() {
        return new TokenModel("xKLaonfDRZu3KLYbToKzWmonYD3F5uKncH+CN8bxVOYqCOnrGJgiW1p0KuuOFsK1", "张三", LocalDateTime.now(),
                LocalDateTime.now());
    }
    /**
     * 获取一个禁用了排他功能的token
     */
    public static TokenModel findDisabledExcludeToken() {
        return new TokenModel("wGI4w0mBAQVmNReoAXv0P5lRj7ZBETjQoAlhRSyL+HY=", "李四", LocalDateTime.now(),
                LocalDateTime.now());
    }
}
