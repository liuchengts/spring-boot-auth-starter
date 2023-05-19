package com.boot.auth.starter;

import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.service.AuthService;
import com.boot.auth.starter.service.LogService;
import com.boot.auth.starter.service.OutJsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    final
    CacheSupport cacheSupport;
    final
    ObjectMapper objectMapper;
    final
    AuthProperties authProperties;
    final
    AuthService authService;
    final
    LogService logService;
    final
    OutJsonService outJsonService;

    public AuthWebConfig(CacheSupport cacheSupport, ObjectMapper objectMapper, AuthProperties authProperties, AuthService authService, LogService logService, OutJsonService outJsonService) {
        this.cacheSupport = cacheSupport;
        this.objectMapper = objectMapper;
        this.authProperties = authProperties;
        this.authService = authService;
        this.logService = logService;
        this.outJsonService = outJsonService;
    }

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
        return new SessionResolver(cacheSupport, objectMapper, authProperties.getTokenPrefix());
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
