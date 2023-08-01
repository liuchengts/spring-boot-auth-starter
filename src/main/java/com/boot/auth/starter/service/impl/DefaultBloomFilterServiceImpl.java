package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.service.BloomFilterService;
import com.boot.auth.starter.support.GuavaBloomSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBloomFilterServiceImpl implements BloomFilterService {
    private final static Logger log = LoggerFactory.getLogger(DefaultBloomFilterServiceImpl.class);
    final
    GuavaBloomSupport guavaBloomSupport;

    public DefaultBloomFilterServiceImpl(GuavaBloomSupport guavaBloomSupport) {
        this.guavaBloomSupport = guavaBloomSupport;
    }

    @Override
    public void put(String value) {
        guavaBloomSupport.put(value);
    }

    @Override
    public void notContainPut(String value) {
        guavaBloomSupport.notContainPut(value);
    }

    @Override
    public Boolean mightContain(String value) {
        return guavaBloomSupport.mightContain(value);
    }
}
