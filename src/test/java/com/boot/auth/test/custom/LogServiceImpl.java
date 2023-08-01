package com.boot.auth.test.custom;

import com.boot.auth.starter.model.OperLogAnnotationEntity;
import com.boot.auth.starter.service.impl.DefaultLogServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 自定义业务log拦截数据处理方案
 */
@Primary
@Component
public class LogServiceImpl extends DefaultLogServiceImpl {
    private final static Logger log = LoggerFactory.getLogger(LogServiceImpl.class);
    @Override
    public void addLog(OperLogAnnotationEntity logEntity) {
        log.info("add log :{}", logEntity);
        //todo 在这里自己定义访问日志处理的方式
    }
}
