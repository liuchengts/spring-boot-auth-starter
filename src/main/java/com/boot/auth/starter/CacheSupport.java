package com.boot.auth.starter;

import org.springframework.stereotype.Component;

/**
 * 缓存
 */
@Component
public abstract class CacheSupport {
    /**
     * 存储
     *
     * @param key         要存储的key
     * @param data        存储的数据json
     * @param overdueTime 过期时间，单位秒
     */
    public void put(String key, String data, Long overdueTime) {

    }

    /**
     * 获取
     *
     * @param key 要获取的key
     * @return 获取内容
     */
    public String get(String key) {
        return null;
    }

    /**
     * 查询过期时间
     *
     * @param key 要查询的key
     * @return 过期时间，单位秒
     */
    public Long getExpire(String key) {
        return 0L;
    }

    /**
     * 移除
     *
     * @param key 要移除的key
     */
    public void remove(String key) {

    }
}
