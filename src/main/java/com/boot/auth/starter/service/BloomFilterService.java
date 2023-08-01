package com.boot.auth.starter.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * 布隆过滤器业务
 */
public interface BloomFilterService {
    /**
     * 向布隆过滤器中添加一个元素
     *
     * @param value 要添加的元素
     */
    void put(String value);

    /**
     * 查找一个元素是否在布隆过滤器中存在，不存在就加入到过滤器中
     *
     * @param value 要查找/加入的元素
     */
    void notContainPut(String value);

    /**
     * 查找一个元素是否在布隆过滤器中存在
     *
     * @param value 要查找的元素
     * @return true表示可能存在 false表示一定不存在
     */
    Boolean mightContain(String value);
}
