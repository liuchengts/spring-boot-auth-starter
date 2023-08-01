package com.boot.auth.starter.exception;

import com.boot.auth.starter.common.RestStatus;

public class AuthException extends RuntimeException {
    private String code;
    private String msg;

    public AuthException() {
        super();
    }

    public AuthException(Exception e) {
        super(e);
        this.msg = e.getMessage();
    }

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(String msg, String code) {
        super(String.join(":", code, msg));
        this.msg = msg;
        this.code = code;
    }

    public AuthException(RestStatus restStatus) {
        super(String.join(":", String.valueOf(restStatus.value()), restStatus.getMsg()));
        this.msg = restStatus.getMsg();
        this.code = String.valueOf(restStatus.value());
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
