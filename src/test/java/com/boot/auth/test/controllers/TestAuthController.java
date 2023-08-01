package com.boot.auth.test.controllers;

import com.boot.auth.starter.annotation.Auth;
import com.boot.auth.starter.annotation.IgnoreLogin;
import com.boot.auth.starter.annotation.NoAuthGetSession;
import com.boot.auth.starter.common.Session;
import com.boot.auth.test.custom.RolesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestAuthController {
    private final static Logger log = LoggerFactory.getLogger(TestAuthController.class);

    @Auth
    @GetMapping("/auth1")
    public Session testAuth1(Session session) {
        log.info("访问到了 auth1");
        return session;
    }

    @NoAuthGetSession
    @GetMapping("/auth2")
    public Session testAuth2(Session session) {
        log.info("访问到了 auth2");
        return session;
    }

    @IgnoreLogin
    @GetMapping("/auth3")
    public Session testAuth3(Session session) {
        //由于不校验登录，所以session为空
        log.info("访问到了 auth3");
        return session;
    }

    @GetMapping("/auth4")
    public Session testAuth4(Session session) {
        log.info("访问到了 auth4");
        return session;
    }

    @Auth(roles = {RolesConstant.USER_1, RolesConstant.USER_2})
    @GetMapping("/auth_r1")
    public Session testAuthR1(Session session) {
        log.info("访问到了 auth_r1");
        return session;
    }

    @NoAuthGetSession(loginRequired = false)
    @GetMapping("/auth_r2")
    public Session testAuthR2(Session session) {
        log.info("访问到了 auth_r2");
        return session;
    }

    @IgnoreLogin(ignore = false)
    @GetMapping("/auth_r3")
    public Session testAuthR3(Session session) {
        log.info("访问到了 auth_r3");
        return session;
    }

}
