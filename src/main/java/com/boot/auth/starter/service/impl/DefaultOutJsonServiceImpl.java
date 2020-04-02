package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.exception.AuthException;
import com.boot.auth.starter.service.OutJsonService;
import com.boot.auth.starter.vo.DefaultResponseVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultOutJsonServiceImpl implements OutJsonService {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public String errorOutJson(String msg, String code) {
        try {
            return objectMapper.writeValueAsString(new DefaultResponseVO(msg, code));
        } catch (Exception e) {
            throw new AuthException(e);
        }
    }
}
