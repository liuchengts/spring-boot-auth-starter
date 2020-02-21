package com.boot.auth.starter.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@ConfigurationProperties(prefix = "info.auth")
public class AuthProperties {
    /**
     * token 前缀
     */
    public String tokenPrefix;
    /**
     * 授权过期时间，单位（天）
     */
    public Long overdueTime;
    /**
     * 域名
     */
    public String domain;
}
