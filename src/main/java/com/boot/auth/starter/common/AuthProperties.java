package com.boot.auth.starter.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "info.auth")
public class AuthProperties {
    /**
     * token 前缀
     */
    public String tokenPrefix;
    /**
     * 授权过期时间，单位（秒）
     */
    public Long overdueTime;
    /**
     * 域名
     */
    public String domain;

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
