package com.boot.auth.starter.vo;

import com.boot.auth.starter.common.RestStatus;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultResponseVO implements Serializable {
    private String code;
    private String msg;

    public DefaultResponseVO(RestStatus restStatus) {
        this.msg = restStatus.getMsg();
        this.code = String.valueOf(restStatus.value());
    }

    public DefaultResponseVO(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public DefaultResponseVO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
