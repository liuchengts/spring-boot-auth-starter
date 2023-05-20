package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.service.CacheService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultCacheServiceImpl implements CacheService {
    private final static org.slf4j.Logger log = LoggerFactory.getLogger(DefaultCacheServiceImpl.class);
    private final static String ERROR = "默认未实现此方法,请自行实现";

    @Override
    public void put(String key, String data) {
        log.warn(ERROR);
    }

    @Override
    public void put(String key, String data, Long overdueTime) {
        log.warn(ERROR);
    }

    @Override
    public String get(String key) {
        log.warn(ERROR);
        return null;
    }

    @Override
    public Long getExpire(String key) {
        log.warn(ERROR);
        return null;
    }

    @Override
    public void remove(String key) {
        log.warn(ERROR);
    }
}
