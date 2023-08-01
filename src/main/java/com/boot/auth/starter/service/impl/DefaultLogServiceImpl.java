package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.model.OperLogAnnotationEntity;
import com.boot.auth.starter.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultLogServiceImpl implements LogService {
    private final static Logger log = LoggerFactory.getLogger(DefaultLogServiceImpl.class);
    @Override
    public void addLog(OperLogAnnotationEntity logEntity) {
        log.debug("add log :{}", logEntity);
    }
}
