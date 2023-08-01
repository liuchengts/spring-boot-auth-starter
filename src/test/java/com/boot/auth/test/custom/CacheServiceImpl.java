package com.boot.auth.test.custom;//package com.boot.auth.example.custom;
//
//import com.boot.auth.starter.common.AuthProperties;
//import com.boot.auth.starter.service.impl.DefaultCacheServiceImpl;
//import com.boot.auth.starter.support.GuavaCacheSupport;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * 自定义redis缓存方案
// */
//@Primary
//@Component
//public class CacheServiceImpl extends DefaultCacheServiceImpl {
//    final
//    StringRedisTemplate stringRedisTemplate;
//    private final static Logger log = LoggerFactory.getLogger(CacheServiceImpl.class);
//    public CacheServiceImpl(GuavaCacheSupport guavaCacheSupport,
//                            AuthProperties authProperties,
//                            StringRedisTemplate stringRedisTemplate) {
//        super(guavaCacheSupport, authProperties);
//        this.stringRedisTemplate = stringRedisTemplate;
//        // 手动关闭 guavaCacheSupport 功能
//        guavaCacheSupport.notEnabled();
//    }
//
//    @Override
//    public void put(String key, String data) {
//        stringRedisTemplate.opsForValue().set(key, data, super.getOverdueTime(), TimeUnit.SECONDS);
//    }
//
//    @Override
//    public String get(String key) {
//        if (super.getExclude() != null
//                && super.getExclude()
//                && !this.exclude(key)) {
//            return null;
//        }
//        return stringRedisTemplate.opsForValue().get(key);
//    }
//
//    @Override
//    public Object excludeGet(String keyExclude) {
//        String value = stringRedisTemplate.opsForValue().get(keyExclude);
//        Object runValue = null;
//        if (StringUtils.hasText(value)) {
//            runValue = value;
//        }
//        return runValue;
//    }
//
//
//    @Override
//    public Long getExpire(String key) {
//        return stringRedisTemplate.getExpire(key);
//    }
//
//    @Override
//    public void remove(String key) {
//        stringRedisTemplate.delete(key);
//    }
//}
