package com.boot.auth.test.controllers;

import com.boot.auth.starter.annotation.Auth;
import com.boot.auth.starter.annotation.OperLog;
import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.Session;
import com.boot.auth.test.custom.OperLogConstant;
import com.boot.auth.test.custom.RolesConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 在业务层中使用权限与日志记录的方式
 */
@RestController
@RequestMapping("/service")
public class ServiceController {
    private final static Logger log = LoggerFactory.getLogger(ServiceController.class);
    /**
     * 增加日志记录
     * 测试获取请求头中的参数
     * 实际使用不必参考此方法
     */
    @OperLog(operType = OperLogConstant.SERVICE1)
    @GetMapping("/1")
    public Object service1(@RequestHeader(value = AuthConstant.HEADER_KEY_PLATFORM, required = false) String platform,
                           @RequestHeader(value = AuthConstant.HEADER_KEY_CHANNEL, required = false) String channel,
                           @RequestHeader(value = AuthConstant.HEADER_KEY_VERSION, required = false) String version,
                           @RequestHeader(value = AuthConstant.HEADER_KEY_DEVICEID, required = false) String deviceId) {
        Map<String, String> map = new HashMap<>();
        map.put(AuthConstant.HEADER_KEY_PLATFORM, platform);
        map.put(AuthConstant.HEADER_KEY_CHANNEL, channel);
        map.put(AuthConstant.HEADER_KEY_VERSION, version);
        map.put(AuthConstant.HEADER_KEY_DEVICEID, deviceId);
        log.info("访问到了 service1:{}", map);
        return map;
    }

    /**
     * 增加日志记录
     * 从session中获取相关信息
     * 正常使用方式
     */
    @OperLog(operType = OperLogConstant.SERVICE2)
    @Auth(roles = RolesConstant.USER_1)
    @GetMapping("/2")
    public Object service2(Session session) {
        log.info("访问到了 service2:{}", session);
        return session;
    }
}
