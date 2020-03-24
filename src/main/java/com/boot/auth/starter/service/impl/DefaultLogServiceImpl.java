package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.model.OperLogAnnotationEntity;
import com.boot.auth.starter.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultLogServiceImpl implements LogService {

    @Override
    public void addLog(OperLogAnnotationEntity logEntity) {
        log.debug("add log :{}", logEntity);
    }
}
