package com.boot.auth.starter.support;

import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.exception.AuthException;
import com.google.common.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author liucheng
 * @version 1.0
 * @description: 基于 guava 的缓存
 * @date 2023/6/3 12:45
 */
@Scope
@Component
public class GuavaCacheSupport {
    private final static Logger log = LoggerFactory.getLogger(GuavaCacheSupport.class);
    final
    AuthProperties authProperties;
    private final int CPU_N = Runtime.getRuntime().availableProcessors();
    private Cache<Object, Object> cache;
    private LoadingCache<Object, Object> loadingCache;
    private CacheLoader<Object, Object> cacheLoader;
    private RemovalListener<Object, Object> removalListener;
    private final Executor EXECUTOR = Executors.newFixedThreadPool(CPU_N);
    private Boolean ENABLE = true;

    public GuavaCacheSupport(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    /**
     * 不启用 GuavaCacheSupport
     */
    public void notEnabled() {
        ENABLE = false;
        log.warn("不启用 GuavaCacheSupport ");
    }

    public CacheStats stats() {
        if (!authProperties.getCacheStats()) {
            log.warn("未启用缓存统计");
            return null;
        }
        CacheStats stats = cache.stats();
        String buffer = "缓存状态查看=>" +
                " [命中次数:" +
                stats.hitCount() +
                "] [数据加载成功次数:" +
                stats.missCount() +
                "] [加载异常数:" +
                stats.loadSuccessCount() +
                "] [加载异常数:" +
                stats.loadExceptionCount() +
                "] [移除缓存次数:" +
                stats.evictionCount() +
                "[ [总加载时间:" +
                stats.totalLoadTime() + "]";
        log.info(buffer);
        return stats;
    }

    public Cache<Object, Object> getCache() {
        autoCache();
        return cache;
    }

    public void setCache(Cache<Object, Object> cache) {
        this.cache = cache;
    }

    public LoadingCache<Object, Object> getLoadingCache() {
        return loadingCache;
    }

    public void setLoadingCache(LoadingCache<Object, Object> loadingCache) {
        this.loadingCache = loadingCache;
    }

    public CacheLoader<Object, Object> getCacheLoader() {
        return cacheLoader;
    }

    /**
     * 使用loadingCache 必须先手动调用，传入加载器方法
     *
     * @param cacheLoader 加载器方法
     */
    public void setCacheLoader(CacheLoader<Object, Object> cacheLoader) {
        this.cacheLoader = cacheLoader;
    }

    public RemovalListener<Object, Object> getRemovalListener() {
        return removalListener;
    }

    public void setRemovalListener(RemovalListener<Object, Object> removalListener) {
        this.removalListener = removalListener;
    }

    /**
     * 自动判断当前可以自动初始化的缓存类型
     */
    public void autoCache() {
        if (!ENABLE) {
            log.warn("未启用 GuavaCacheSupport ");
            return;
        }
        if (authProperties.getLoadingCache()) {
            if (this.loadingCache == null) createCache();
        } else {
            if (this.cache == null) createCache();
        }
        stats();
    }

    private void createCache() {
        CacheBuilder<Object, Object> cacheBuilder;
        //设置缓存的异步移除通知
        if (this.getRemovalListener() != null) {
            cacheBuilder = CacheBuilder.newBuilder().removalListener(RemovalListeners.asynchronous(removalListener, EXECUTOR));
        } else {
            cacheBuilder = CacheBuilder.newBuilder();
        }
        //设置并发级别,并发级别是指可以同时写缓存的线程数
        cacheBuilder.concurrencyLevel(CPU_N);
        //设置缓存容器的初始容量为100
        if (authProperties.getCacheInitialCapacity() != null
                && authProperties.getCacheInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(authProperties.getCacheInitialCapacity());
        }
        //设置缓存最大容量为1000，超过1000之后就会按照LRU最近虽少使用算法来移除缓存项
        if (authProperties.getCacheMaximumSize() != null
                && authProperties.getCacheMaximumSize() > 0) {
            cacheBuilder.maximumSize(authProperties.getCacheMaximumSize());
        }
        //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
        if (authProperties.getCacheStats() != null
                && authProperties.getCacheStats()) {
            cacheBuilder.recordStats();
        }
        //设置写缓存后n秒钟过期
        if (authProperties.getOverdueTime() > 0) {
            cacheBuilder.expireAfterWrite(authProperties.getOverdueTime(), TimeUnit.SECONDS);
        }
        // 设置加载缓存的方法
        if (authProperties.getLoadingCache() != null
                && authProperties.getLoadingCache()) {
            if (this.getCacheLoader() == null) {
                throw new AuthException(RestStatus.SYSTEM_CACHE_ERROR);
            }
            this.setLoadingCache(cacheBuilder.build(this.getCacheLoader()));
        } else {
            this.setCache(cacheBuilder.build());
        }
        log.info("GuavaCache [创建完成]");
    }

//    public static void main(String[] args) throws InterruptedException {
//        AuthProperties authProperties = new AuthProperties();
//        authProperties.setOverdueTime(99999999L);
//        authProperties.setCacheInitialCapacity(100);
//        authProperties.setCacheMaximumSize(200L);
//        authProperties.setCacheStats(true);
//        GuavaCacheSupport cacheSupport = new GuavaCacheSupport(authProperties);
//
//        cacheSupport.getCache().put("111111,," + System.currentTimeMillis(), "我是内容");
//        Thread.sleep(3000l);
//        cacheSupport.getCache().put("111111,," + System.currentTimeMillis(), "我是内容");
//        log.info("map:{}", cacheSupport.getCache().asMap());
//    }
}
