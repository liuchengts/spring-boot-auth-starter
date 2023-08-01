package com.boot.auth.test.custom;

import com.boot.auth.starter.exception.AuthException;
import com.boot.auth.starter.service.impl.DefaultOutJsonServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 自定义异常输出方案
 */
@Primary
@Component
public class OutJsonServiceImpl extends DefaultOutJsonServiceImpl {
    final
    ObjectMapper objectMapper;
    private final static Logger log = LoggerFactory.getLogger(OutJsonServiceImpl.class);

    public OutJsonServiceImpl(ObjectMapper objectMapper) {
        super(objectMapper);
        this.objectMapper = objectMapper;
    }

    @Override
    public String errorOutJson(String msg, String code) {
        //todo 在这里自己定义被权限拦截后的数据返回格式
        try {
            return objectMapper.writeValueAsString(msg);
        } catch (Exception e) {
            throw new AuthException(e);
        }
    }
}
