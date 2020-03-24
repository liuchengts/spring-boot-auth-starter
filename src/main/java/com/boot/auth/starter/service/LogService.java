package com.boot.auth.starter.service;

import com.boot.auth.starter.model.OperLogAnnotationEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LogService {

    /**
     * 增加一个Log日志
     *
     * @param logEntity 要增加的内容
     */
    void addLog(OperLogAnnotationEntity logEntity);
}
