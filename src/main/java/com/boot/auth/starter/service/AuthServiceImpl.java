package com.boot.auth.starter.service;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.common.RestStatus;
import com.boot.auth.starter.exception.AuthException;
import com.boot.auth.starter.utils.AESUtil;
import com.boot.auth.starter.utils.CookieUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AuthServiceImpl implements AuthService {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AuthProperties authProperties;
    final static String TOKEN_NAME = AuthConstant.HEAD_TOKEN_NAME.toLowerCase();

    @Override
    public String auth(String group, String userNo, String roles, Map<String, Object> parameters, HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, String> oldTokenMap = analysisToken(request);
        String key = userNo + AuthConstant.HEAD_TOKEN_SEPARATOR + group;
        //生成token
        String token = AESUtil.encrypt(key + AuthConstant.HEAD_TOKEN_SEPARATOR + System.currentTimeMillis(), authProperties.getDomain());
        if (parameters == null) parameters = new HashMap<>();
        parameters.put(AuthConstant.SESSION_USER_NO, userNo);
        parameters.put(AuthConstant.SESSION_ROLES, roles);
        redisTemplate.opsForValue().set(authProperties.getTokenPrefix() + key, objectMapper.writeValueAsString(parameters), authProperties.getOverdueTime(), TimeUnit.DAYS);
        CookieUtils.setCookie(request, response, TOKEN_NAME, token);
        response.setHeader(TOKEN_NAME, token);
        //删除原有的token
        delToken(oldTokenMap);
        return token;
    }

    private void delToken(Map<String, String> oldTokenMap) {
        if (oldTokenMap.isEmpty() || !oldTokenMap.containsKey(AuthConstant.MAP_KEY_KEY)) return;
        redisTemplate.delete(authProperties.getTokenPrefix() + oldTokenMap.get(AuthConstant.MAP_KEY_KEY));
    }

    @Override
    public Map<String, String> analysisToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);
        if (StringUtils.isEmpty(token)) token = CookieUtils.getCookieValue(request, TOKEN_NAME);
        return analysisToken(token);
    }

    @Override
    public Map<String, String> analysisToken(String token) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(token)) return map;
        try {
            String decryptToken = AESUtil.decrypt(token, authProperties.getDomain());
            assert decryptToken != null;
            String[] keys = decryptToken.split(AuthConstant.HEAD_TOKEN_SEPARATOR);
            map.put(AuthConstant.MAP_KEY_TOKEN, token);
            map.put(AuthConstant.MAP_KEY_USER_NO, keys[0]);
            map.put(AuthConstant.MAP_KEY_GROUP, keys[1]);
            map.put(AuthConstant.MAP_KEY_TIME, keys[2]);
            map.put(AuthConstant.MAP_KEY_KEY, keys[0] + AuthConstant.HEAD_TOKEN_SEPARATOR + keys[1]);
        } catch (Exception e) {
            log.error("解密token失败", e);
        }
        return map;
    }

    @Override
    public void deleteAuth(HttpServletRequest request) {
        delToken(analysisToken(request));
    }

    @Override
    public Boolean checkToken(HttpServletRequest request) {
        Map<String, String> tokenMap = analysisToken(request);
        if (tokenMap.isEmpty() || !tokenMap.containsKey(AuthConstant.MAP_KEY_KEY)) return false;
        try {
            Long expire = redisTemplate.getExpire(authProperties.getTokenPrefix() + tokenMap.get(AuthConstant.MAP_KEY_KEY), TimeUnit.SECONDS);
            if (expire <= 0) return false;
        } catch (Exception e) {
            throw new AuthException(RestStatus.SYSTEM_ERROR);
        }
        return true;
    }
}
