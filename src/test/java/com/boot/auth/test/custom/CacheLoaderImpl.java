package com.boot.auth.test.custom;

import com.boot.auth.starter.common.AuthConstant;
import com.boot.auth.starter.common.AuthProperties;
import com.boot.auth.starter.support.GuavaBloomSupport;
import com.boot.auth.starter.support.GuavaCacheSupport;
import com.boot.auth.test.common.AccountModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.RemovalListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用 CacheLoader
 *
 * @author liucheng
 * @version 1.0
 * @description: 自定义缓存加载器
 * @date 2023/6/5 17:09
 */
@Component
public class CacheLoaderImpl {
    final
    GuavaCacheSupport guavaCacheSupport;
    ObjectMapper objectMapper;

    public CacheLoaderImpl(GuavaCacheSupport guavaCacheSupport,
                           ObjectMapper objectMapper) {
        this.guavaCacheSupport = guavaCacheSupport;
        this.objectMapper = objectMapper;
    }

    private final static Logger log = LoggerFactory.getLogger(CacheLoaderImpl.class);

    @PostConstruct
    public void init() {
        // 设置全局加载器
        guavaCacheSupport.setCacheLoader(new GlobalCacheLoader());
        // 设置全局移除监听器
        guavaCacheSupport.setRemovalListener(getRemovalListener());
        log.info("启用 CacheLoader");
    }

    /**
     * db查询
     *
     * @param key 查询条件
     * @return 查询结果
     */
    public String getDb(String key) throws JsonProcessingException {
        // 根据key 从数据库中查询出来
        Map<String, Object> map = new HashMap<>();
        AccountModel accountModel = AccountModel.findAccount();
        map.put(AuthConstant.SESSION_NICK_NAME, accountModel.getNickName());
        map.put(AuthConstant.SESSION_USER_NO, accountModel.getUserNo());
        map.put(AuthConstant.SESSION_ROLES, accountModel.getRoles());
        map.put(AuthConstant.MAP_KEY_GROUP, accountModel.getGroup());
        return objectMapper.writeValueAsString(map);
    }

    /**
     * 全局缓存加载器
     */
    public class GlobalCacheLoader extends CacheLoader<Object, Object> {

        @Override
        public Object load(Object key) throws Exception {
            // 这里开始加载缓存数据
            return getDb(key.toString());
        }
    }

    /**
     * 全局移除事件监听
     */
    private RemovalListener<Object, Object> getRemovalListener() {
        return notification -> {
            log.warn("移除缓存 key:{} value:{}", notification.getKey(), notification.getValue());
            // 下面是移除了缓存需要做的事情
        };
    }
}
