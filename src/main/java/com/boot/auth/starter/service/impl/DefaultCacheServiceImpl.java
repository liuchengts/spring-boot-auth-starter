package com.boot.auth.starter.service.impl;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.exception.AuthException;
import com.boot.auth.starter.service.CacheService;
import com.boot.auth.starter.support.GuavaBloomSupport;
import com.boot.auth.starter.support.GuavaCacheSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 在使用本类时，需要注意以下问题:
 * 1、若使用 Cache 而非 LoadingCache ，可以直接使用本类，无需任何改动
 * 2、使用 LoadingCache 时，需要自行在 GuavaCacheSupport 中调用 setCacheLoader 后才能使用该类其他方法
 * 3、GuavaCacheSupport 中的 setRemovalListener 是可选的
 * 4、在 Guava 中没有直接实现 expire ，只是判断了是否存在，请自行实现
 * 5、exclude 选项不是必须的
 * 6、若本类中有任何方法不适用，请自行复写它。若先使用其他缓存，请直接复写全部方法
 */
@Component
public class DefaultCacheServiceImpl implements CacheService {
    private final static Logger log = LoggerFactory.getLogger(DefaultCacheServiceImpl.class);
    final
    GuavaCacheSupport guavaCacheSupport;
    GuavaBloomSupport guavaBloomSupport;
    AuthProperties authProperties;

    public DefaultCacheServiceImpl(GuavaCacheSupport guavaCacheSupport,
                                   GuavaBloomSupport guavaBloomSupport,
                                   AuthProperties authProperties) {
        this.guavaCacheSupport = guavaCacheSupport;
        this.guavaBloomSupport = guavaBloomSupport;
        this.authProperties = authProperties;
    }

    @Override
    public Long getOverdueTime() {
        return authProperties.getOverdueTime();
    }

    @Override
    public Boolean getExclude() {
        return authProperties.getEnableExclude();
    }

    @Override
    public void put(String key, String data) {
        if (authProperties.getGuavaCache().getEnableLoadingCache()) {
            guavaCacheSupport.getLoadingCache().put(key, data);
        } else {
            guavaCacheSupport.getCache().put(key, data);
        }
        guavaBloomSupport.notContainPut(key);
    }

    @Override
    public void put(String key, String data, Long overdueTime) {
        this.put(key, data);
    }

    @Override
    public String get(String key) {
        return get(key, authProperties.getEnableExclude());
    }

    @Override
    public String get(String key, Callable<Object> loader) throws ExecutionException {
        return get(key, loader, authProperties.getEnableExclude());

    }

    @Override
    public String get(String key, Callable<Object> loader, Boolean enableExclude) throws ExecutionException {
        if (enableExclude && !this.exclude(key)) {
            return null;
        }
        Object obj = null;
        if (authProperties.getGuavaCache().getEnableLoadingCache()) {
            try {
                if (loader == null) {
                    Boolean bloomRes = guavaBloomSupport.mightContain(key);
                    if (bloomRes != null && !bloomRes) {
                        log.warn("BloomFilter 拦截 不存在");
                        return null;
                    }
                    obj = guavaCacheSupport.getLoadingCache().get(key);
                } else {
                    obj = guavaCacheSupport.getLoadingCache().get(key, loader);
                }
            } catch (ExecutionException e) {
                log.error("LoadingCache error key:{}", key, e);
            }
        } else {
            if (loader == null) {
                Boolean bloomRes = guavaBloomSupport.mightContain(key);
                if (bloomRes != null && !bloomRes) {
                    log.warn("BloomFilter 拦截 不存在");
                    return null;
                }
                obj = guavaCacheSupport.getCache().getIfPresent(key);
            } else {
                obj = guavaCacheSupport.getCache().get(key, loader);
            }
        }
        if (obj == null) return null;
        return obj.toString();

    }

    @Override
    public String get(String key, Boolean enableExclude) {
        Boolean bloomRes = guavaBloomSupport.mightContain(key);
        if (bloomRes != null && !bloomRes) {
            log.warn("BloomFilter 拦截 不存在");
            return null;
        }
        if (enableExclude && !this.exclude(key)) {
            return null;
        }
        Object obj = null;
        if (authProperties.getGuavaCache().getEnableLoadingCache()) {
            try {
                obj = guavaCacheSupport.getLoadingCache().get(key);
            } catch (ExecutionException e) {
                log.error("LoadingCache error key:{}", key, e);
            }
        } else {
            obj = guavaCacheSupport.getCache().getIfPresent(key);
        }
        if (obj == null) return null;
        return obj.toString();
    }

    @Override
    public Object excludeGet(String keyExclude) {
        return get(keyExclude, false);
    }

    @Override
    public Object excludeGet(String keyExclude, Callable<Object> loader) {
        try {
            return get(keyExclude, loader, false);
        } catch (ExecutionException e) {
            log.error("excludeGet [" + keyExclude + "]", e);
        }
        return null;
    }

    @Override
    public Long getExpire(String key) {
        if (this.get(key) != null) return 1L;
        return 0L;
    }

    @Override
    public void remove(String key) {
        if (authProperties.getGuavaCache().getEnableLoadingCache()) {
            guavaCacheSupport.getLoadingCache().invalidate(key);
        } else {
            guavaCacheSupport.getCache().invalidate(key);
        }
    }

    @Override
    public boolean exclude(String key) {
        try {
            return this.exclude(key, null);
        } catch (ExecutionException e) {
            log.error("exclude error:", e);
        }
        return false;
    }

    @Override
    public boolean exclude(String key, Callable<Object> loader) throws ExecutionException {
        String[] array = key.split(AuthConstant.HEAD_TOKEN_SEPARATOR);
        if (array.length != 4) throw new AuthException(RestStatus.SYSTEM_CACHE_KEY_ERROR);
        String userNo = array[0];
        String excludeSerial = array[3].substring(1);
        String keyExclude = "exclude-" + userNo;
        String excludeSerialLast = "";
        if (loader != null) {
            Object groupLastObj = excludeGet(keyExclude, loader);
            excludeSerialLast = groupLastObj.toString();
        } else {
            Object groupLastObj = excludeGet(keyExclude);
            if (groupLastObj != null) {
                excludeSerialLast = groupLastObj.toString();
            }
        }
        if (StringUtils.isEmpty(excludeSerialLast)) {
            this.put(keyExclude, excludeSerial);
            return true;
        }
        if (Long.parseLong(excludeSerial) >= Long.parseLong(excludeSerialLast)) {
            this.put(keyExclude, excludeSerial);
            return true;
        }
        return false;
    }
}
