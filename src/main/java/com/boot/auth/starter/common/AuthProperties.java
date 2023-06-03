package com.boot.auth.starter.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "info.auth")
public class AuthProperties {
    /**
     * token 前缀
     */
    private String tokenPrefix;
    /**
     * 授权过期时间，单位（秒）
     */
    private Long overdueTime;
    /**
     * 域名
     */
    private String domain;

    /**
     * 开启排他授权
     */
    private Boolean exclude;

    /**
     * 设置缓存容器的初始容量
     */
    private Integer cacheInitialCapacity;

    /**
     * 设置缓存最大容量，超过之后就会按照LRU最近虽少使用算法来移除缓存项
     */
    private Long cacheMaximumSize;

    /**
     * 开启缓存统计
     */
    private Boolean cacheStats;

    /**
     * 启用loadingCache
     */
    private Boolean loadingCache;

    public Boolean getExclude() {
        if (exclude == null) exclude = false;
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }

    public Boolean getLoadingCache() {
        if (loadingCache == null) loadingCache = false;
        return loadingCache;
    }

    public void setLoadingCache(Boolean loadingCache) {
        this.loadingCache = loadingCache;
    }

    public Boolean getCacheStats() {
        if (cacheStats == null) cacheStats = false;
        return cacheStats;
    }

    public void setCacheStats(Boolean cacheStats) {
        this.cacheStats = cacheStats;
    }

    public Integer getCacheInitialCapacity() {
        return cacheInitialCapacity;
    }

    public void setCacheInitialCapacity(Integer cacheInitialCapacity) {
        this.cacheInitialCapacity = cacheInitialCapacity;
    }

    public Long getCacheMaximumSize() {
        return cacheMaximumSize;
    }

    public void setCacheMaximumSize(Long cacheMaximumSize) {
        this.cacheMaximumSize = cacheMaximumSize;
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
}
