package com.boot.auth.starter.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 缓存业务
 */
public interface CacheService {
    /**
     * 获取设置的缓存过期时间
     *
     * @return 缓存过期时间，单位秒
     */
    Long getOverdueTime();

    /**
     * 获取设置的排他属性
     *
     * @return true表示启用排他
     */
    Boolean getExclude();

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
     * 获取
     *
     * @param key           要获取的key
     * @param loader        加载方法
     * @param enableExclude 是否启用排除验证
     * @return 获取内容
     */
    String get(String key, Callable<Object> loader, Boolean enableExclude) throws ExecutionException;

    /**
     * 获取
     *
     * @param key           要获取的key
     * @param enableExclude 是否启用排除验证
     * @return 获取内容
     */
    String get(String key, Boolean enableExclude);

    /**
     * 排他性 get方法
     *
     * @param keyExclude 要获取的key
     * @return 获取内容
     */
    Object excludeGet(String keyExclude);

    /**
     * 排他性 get方法
     *
     * @param keyExclude 要获取的key
     * @param loader     加载方法
     * @return 获取内容
     */
    Object excludeGet(String keyExclude, Callable<Object> loader);

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
