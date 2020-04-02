package com.boot.auth.starter.common;

import java.util.Optional;

public class LogicSession {
    /**
     * 登录有效，true有效
     */
    private Boolean validLogin = false;
    /**
     * token有效，true有效
     */
    private Boolean validToken = false;
    /**
     * session
     */
    private Optional<Session> sessionOptional = Optional.empty();

    public Boolean getValidLogin() {
        return validLogin;
    }

    public void setValidLogin(Boolean validLogin) {
        this.validLogin = validLogin;
    }

    public Boolean getValidToken() {
        return validToken;
    }

    public void setValidToken(Boolean validToken) {
        this.validToken = validToken;
    }

    public Optional<Session> getSessionOptional() {
        return sessionOptional;
    }

    public void setSessionOptional(Optional<Session> sessionOptional) {
        this.sessionOptional = sessionOptional;
    }
}
