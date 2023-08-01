package com.boot.auth.starter.support;

import com.boot.auth.starter.common.AuthProperties;
import com.google.common.hash.BloomFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

import static com.google.common.hash.Funnels.stringFunnel;

/**
 * 基于 guava 的布隆过滤器
 * 2023/6/3 12:45
 *
 * @author liucheng
 * @version 1.0
 */
@SuppressWarnings("all")
@Scope
@Component
public class GuavaBloomSupport {
    private final static Logger log = LoggerFactory.getLogger(GuavaBloomSupport.class);
    final
    AuthProperties authProperties;

    public GuavaBloomSupport(AuthProperties authProperties) {
        this.authProperties = authProperties;
        if (authProperties.getBloomFilter() == null) {
            log.info("不启用 Guava BloomFilter");
        } else {
            createFilter();
        }
    }

    private BloomFilter<String> stringBloomFilter;

    /**
     * 创建一个过滤器
     */
    private void createFilter() {
        log.info("创建 Guava BloomFilter expectedInsertions:{} fpp:{}",
                authProperties.getBloomFilter().getExpectedInsertions(),
                authProperties.getBloomFilter().getFpp());
        stringBloomFilter = BloomFilter.create(stringFunnel(Charset.defaultCharset()),
                authProperties.getBloomFilter().getExpectedInsertions(),
                authProperties.getBloomFilter().getFpp());
    }

    /**
     * 判断是否创建/启用过滤器
     *
     * @return false 表示未启用
     */
    private boolean isFilter() {
        if (stringBloomFilter == null) {
            return false;
        }
        return true;
    }

    /**
     * 向布隆过滤器中添加一个元素
     *
     * @param value 要添加的元素
     */
    public void put(String value) {
        if (!isFilter()) return;
        stringBloomFilter.put(value);
    }

    /**
     * 查找一个元素是否在布隆过滤器中存在
     *
     * @param value 要查找的元素
     * @return true表示可能存在 false表示一定不存在
     */
    public Boolean mightContain(String value) {
        if (!isFilter()) return null;
        return stringBloomFilter.mightContain(value);
    }

    /**
     * 查找一个元素是否在布隆过滤器中存在，不存在就加入到过滤器中
     *
     * @param value 要查找/加入的元素
     */
    public void notContainPut(String value) {
        Boolean mightContain = mightContain(value);
        if (mightContain != null && !mightContain) {
            put(value);
        }
    }
}
