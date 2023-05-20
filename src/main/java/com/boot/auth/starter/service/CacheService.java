package com.boot.auth.starter.service;

/**
 * 缓存业务
 */
public interface CacheService {
    /**
     * 存储
     *
     * @param key  要存储的key
     * @param data 存储的数据json
     */
    void put(String key, String data);

    /**
     * 存储
     *
     * @param key         要存储的key
     * @param data        存储的数据json
     * @param overdueTime 过期时间，单位秒
     */
    void put(String key, String data, Long overdueTime);

    /**
     * 获取
     *
     * @param key 要获取的key
     * @return 获取内容
     */
    String get(String key);

    /**
     * 查询过期时间
     *
     * @param key 要查询的key
     * @return 过期时间，单位秒
     */
    Long getExpire(String key);

    /**
     * 移除
     *
     * @param key 要移除的key
     */
    void remove(String key);
}
