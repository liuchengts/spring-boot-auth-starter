package com.boot.auth.starter.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info.auth")
public class AuthProperties {
    /**
     * （必须）token 前缀
     */
    private String tokenPrefix;
    /**
     * （必须）授权过期时间，单位（秒）
     */
    private Long overdueTime;
    /**
     * （必须）域名
     */
    private String domain;

    /**
     * （非必须）开启排他授权
     * 开启后同一个账户只有最后登录令牌有效，前面生成的令牌会被废弃
     * 默认不开启，即只要认证成功，在令牌有效期内都是有效的（一个账户可以多端同时登录）
     */
    private Boolean enableExclude;

    /**
     * （非必须）Guava 缓存设置
     */
    private GuavaCache guavaCache;

    /**
     * （非必须）启用布隆过滤器
     */
    private BloomFilter bloomFilter;

    public static class BloomFilter {
        /**
         * （非必须）预期插入数量，默认 1000个
         */
        private Long expectedInsertions;
        /**
         * （非必须）允许的错误概率，必须大于 0.0 默认值 0.01
         */
        private Double fpp;

        public Long getExpectedInsertions() {
            if (expectedInsertions == null || expectedInsertions <= 0) expectedInsertions = 1000L;
            return expectedInsertions;
        }

        public void setExpectedInsertions(Long expectedInsertions) {
            this.expectedInsertions = expectedInsertions;
        }

        public Double getFpp() {
            if (fpp == null || fpp - 0 <= 0) fpp = 0.01;
            return fpp;
        }

        public void setFpp(Double fpp) {
            this.fpp = fpp;
        }
    }

    public static class GuavaCache {
        /**
         * （非必须）设置缓存容器的初始容量
         */
        private Integer cacheInitialCapacity;

        /**
         * （非必须）设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
         */
        private Long cacheMaximumSize;

        /**
         * （非必须）开启缓存统计
         */
        private Boolean enableCacheStats;

        /**
         * （非必须）启用loadingCache
         */
        private Boolean enableLoadingCache;

        public Integer getCacheInitialCapacity() {
            if (cacheInitialCapacity == null) cacheInitialCapacity = 0;
            return cacheInitialCapacity;
        }

        public void setCacheInitialCapacity(Integer cacheInitialCapacity) {
            this.cacheInitialCapacity = cacheInitialCapacity;
        }

        public Long getCacheMaximumSize() {
            if (cacheMaximumSize == null) cacheMaximumSize = 0L;
            return cacheMaximumSize;
        }

        public void setCacheMaximumSize(Long cacheMaximumSize) {
            this.cacheMaximumSize = cacheMaximumSize;
        }

        public Boolean getEnableCacheStats() {
            if (enableCacheStats == null) enableCacheStats = false;
            return enableCacheStats;
        }

        public void setEnableCacheStats(Boolean enableCacheStats) {
            this.enableCacheStats = enableCacheStats;
        }

        public Boolean getEnableLoadingCache() {
            if (enableLoadingCache == null) enableLoadingCache = false;
            return enableLoadingCache;
        }

        public void setEnableLoadingCache(Boolean enableLoadingCache) {
            this.enableLoadingCache = enableLoadingCache;
        }
    }

    public Boolean getEnableExclude() {
        if (enableExclude == null) enableExclude = false;
        return enableExclude;
    }

    public void setEnableExclude(Boolean enableExclude) {
        this.enableExclude = enableExclude;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Long getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(Long overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public GuavaCache getGuavaCache() {
        if (guavaCache == null) guavaCache = new GuavaCache();
        return guavaCache;
    }

    public void setGuavaCache(GuavaCache guavaCache) {
        this.guavaCache = guavaCache;
    }

    public BloomFilter getBloomFilter() {
        return bloomFilter;
    }

    public void setBloomFilter(BloomFilter bloomFilter) {
        this.bloomFilter = bloomFilter;
    }
}
