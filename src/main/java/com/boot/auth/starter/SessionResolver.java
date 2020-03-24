package com.boot.auth.starter;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.common.Session;
import com.boot.auth.starter.exception.AuthException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class SessionResolver {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final String tokenPrefix;

    public SessionResolver(StringRedisTemplate redisTemplate, ObjectMapper objectMapper, String tokenPrefix) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.tokenPrefix = tokenPrefix;
    }

    Optional<Session> resolve(Map<String, String> tokenMap, String platform) {
        if (tokenMap.isEmpty() || !tokenMap.containsKey(AuthConstant.MAP_KEY_KEY)) {
            return Optional.empty();
        }
        String user = redisTemplate.opsForValue().get(tokenPrefix + tokenMap.get(AuthConstant.MAP_KEY_KEY));
        if (user == null || user.trim().isEmpty()) {
            return Optional.empty();
        }
        try {
            JsonNode node = objectMapper.readTree(user);
            String nickName = node.path(AuthConstant.SESSION_NICK_NAME).asText();
            String openId = node.path(AuthConstant.SESSION_OPEN_ID).asText();
            String unionId = node.path(AuthConstant.SESSION_UNION_ID).asText();
            String userNo = node.path(AuthConstant.SESSION_USER_NO).asText();
            String avatar = node.path(AuthConstant.SESSION_AVATAR).asText();
            String roles = node.path(AuthConstant.SESSION_ROLES).asText();
            String mobile = node.path(AuthConstant.SESSION_MOBILE).asText();
            String obj = node.path(AuthConstant.SESSION_OBJECT).asText();
            Session session = Session.builder()
                    .username(nickName)
                    .unionId(unionId)
                    .openId(openId)
                    .userNo(userNo)
                    .avatar(avatar)
                    .roles(roles)
                    .mobile(mobile)
                    .platform(platform)
                    .obj(obj)
                    .build();
            return Optional.of(session);
        } catch (IOException e) {
            throw new AuthException(RestStatus.SYSTEM_ERROR);
        }
    }
}
