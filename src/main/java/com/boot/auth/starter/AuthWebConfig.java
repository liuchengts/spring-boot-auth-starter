package com.boot.auth.starter;

import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.service.AuthService;
import com.boot.auth.starter.vo.AResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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
        return new AuthInterceptor(sessionResolver(), loginRequired(), tokenInvalid(), authNoInvalid(), authService);
    }

    @Bean
    SessionResolver sessionResolver() {
        return new SessionResolver(redisTemplate, objectMapper, authProperties.getTokenPrefix());
    }

    @Bean
    String loginRequired() {
        AResponse aResponse = new AResponse(RestStatus.USER_NOLOGIIN);
        aResponse.setSuccessFalse();
        try {
            return objectMapper.writeValueAsString(aResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    String tokenInvalid() {
        AResponse aResponse = new AResponse(RestStatus.USER_TOKEN_INVALID);
        aResponse.setSuccessFalse();
        try {
            return objectMapper.writeValueAsString(aResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    String authNoInvalid() {
        AResponse aResponse = new AResponse(RestStatus.AUTH_NO);
        aResponse.setSuccessFalse();
        try {
            return objectMapper.writeValueAsString(aResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}