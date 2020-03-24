package com.boot.auth.starter;

import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.service.AuthService;
import com.boot.auth.starter.service.LogService;
import com.boot.auth.starter.service.OutJsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthProperties authProperties;
    @Autowired
    AuthService authService;
    @Autowired
    LogService logService;
    @Autowired
    OutJsonService outJsonService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SessionArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor());
    }


    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor(sessionResolver(), loginRequired(), tokenInvalid(), authNoInvalid(), authService, logService);
    }

    @Bean
    SessionResolver sessionResolver() {
        return new SessionResolver(redisTemplate, objectMapper, authProperties.getTokenPrefix());
    }

    @Bean
    String loginRequired() {
        return outJsonService.errorOutJson(RestStatus.USER_NOLOGIIN.getMsg(), String.valueOf(RestStatus.USER_NOLOGIIN.value()));
    }

    @Bean
    String tokenInvalid() {
        return outJsonService.errorOutJson(RestStatus.USER_TOKEN_INVALID.getMsg(), String.valueOf(RestStatus.USER_TOKEN_INVALID.value()));
    }

    @Bean
    String authNoInvalid() {
        return outJsonService.errorOutJson(RestStatus.AUTH_NO.getMsg(), String.valueOf(RestStatus.AUTH_NO.value()));
    }
}