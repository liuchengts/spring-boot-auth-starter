package com.boot.auth.starter;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.LogicSession;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.common.Session;
import com.boot.auth.starter.exception.AuthException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class SessionResolver {
    private final CacheSupport cacheSupport;
    private final ObjectMapper objectMapper;
    private final String tokenPrefix;

    public SessionResolver(CacheSupport cacheSupport, ObjectMapper objectMapper, String tokenPrefix) {
        this.cacheSupport = cacheSupport;
        this.objectMapper = objectMapper;
        this.tokenPrefix = tokenPrefix;
    }

    LogicSession resolve(Map<String, String> tokenMap, String platform, String version, String ip) {
        LogicSession logicSession = new LogicSession();
        if (tokenMap.isEmpty() || !tokenMap.containsKey(AuthConstant.MAP_KEY_KEY)) return logicSession;
        String user = cacheSupport.get(tokenPrefix + tokenMap.get(AuthConstant.MAP_KEY_KEY));
        if (user == null || user.trim().isEmpty()) return logicSession;
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
            Session session = new Session();
            session.setUsername(nickName);
            session.setUnionId(unionId);
            session.setOpenId(openId);
            session.setUserNo(userNo);
            session.setAvatar(avatar);
            session.setRoles(roles);
            session.setMobile(mobile);
            session.setPlatform(platform);
            session.setVersion(version);
            session.setObj(obj);
            session.setIp(ip);
            logicSession.setValidLogin(true);
            logicSession.setValidToken(true);
            logicSession.setSessionOptional(Optional.of(session));
            return logicSession;
        } catch (IOException e) {
            throw new AuthException(RestStatus.SYSTEM_ERROR);
        }
    }
}
