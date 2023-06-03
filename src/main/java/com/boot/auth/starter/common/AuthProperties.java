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
     *  开启后同一个账户只有最后登录令牌有效，前面生成的令牌会被废弃
     *  默认不开启，即只要认证成功，在令牌有效期内都是有效的（一个账户可以多端同时登录）
     */
    private Boolean exclude;

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
    private Boolean cacheStats;

    /**
     * （非必须）启用loadingCache
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
