package com.boot.auth.starter.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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
     * 获取
     *
     * @param key    要获取的key
     * @param loader 加载方法
     * @return 获取内容
     * 执行loader可能导致的异常
     */
    String get(String key, Callable<Object> loader) throws ExecutionException;

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

    /**
     * 排他
     * 业务解释：用于当一个账号多次登录时，只保留最后一次登录的授权有效
     *
     * @param key 要排除的key
     * @return true 表示通过 false 表示授权已被排除，不可用
     */
    boolean exclude(String key);

    /**
     * 排他
     * 业务解释：用于当一个账号多次登录时，只保留最后一次登录的授权有效
     *
     * @param key    要排除的key
     * @param loader 加载方法
     * @return true 表示通过 false 表示授权已被排除，不可用
     * 执行loader可能导致的异常
     */
    boolean exclude(String key, Callable<Object> loader) throws ExecutionException;

}
