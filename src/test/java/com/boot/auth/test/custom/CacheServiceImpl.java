package com.boot.auth.test.custom;

import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.service.BloomFilterService;
import com.boot.auth.starter.service.impl.DefaultCacheServiceImpl;
import com.boot.auth.starter.support.GuavaBloomSupport;
import com.boot.auth.starter.support.GuavaCacheSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * 自定义redis缓存方案
 */
@Primary
@Component
public class CacheServiceImpl extends DefaultCacheServiceImpl {
    final
    StringRedisTemplate stringRedisTemplate;
    BloomFilterService bloomFilterService;
    private final static Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);

    public CacheServiceImpl(GuavaCacheSupport guavaCacheSupport,
                            AuthProperties authProperties,
                            BloomFilterService bloomFilterService,
                            StringRedisTemplate stringRedisTemplate) {
        super(guavaCacheSupport, bloomFilterService, authProperties);
        this.stringRedisTemplate = stringRedisTemplate;
        this.bloomFilterService = bloomFilterService;
        // 手动关闭 guavaCacheSupport 功能
        guavaCacheSupport.disabled();
    }

    @Override
    public void put(String key, String data) {
        stringRedisTemplate.opsForValue().set(key, data, super.getOverdueTime(), TimeUnit.SECONDS);
        bloomFilterService.notContainPut(key);
    }

    @Override
    public String get(String key) {
        Boolean bloomRes = bloomFilterService.mightContain(key);
        if (bloomRes != null && !bloomRes) {
            log.warn("BloomFilter 不存在");
            return null;
        }
        if (super.getExclude() && !this.exclude(key)) {
            return null;
        }
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Object excludeGet(String keyExclude) {
        Boolean bloomRes = bloomFilterService.mightContain(keyExclude);
        if (bloomRes != null && !bloomRes) {
            log.warn("BloomFilter 不存在");
            return null;
        }
        String value = stringRedisTemplate.opsForValue().get(keyExclude);
        Object runValue = null;
        if (StringUtils.hasText(value)) {
            runValue = value;
        }
        return runValue;
    }


    @Override
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    @Override
    public void remove(String key) {
        stringRedisTemplate.delete(key);
    }
}
