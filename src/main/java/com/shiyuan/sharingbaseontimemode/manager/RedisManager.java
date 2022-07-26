package com.shiyuan.sharingbaseontimemode.manager;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author MUSI
 * @Date 2022/7/26 10:02 PM
 * @Description
 * @Version
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisManager {

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean setCache(String key, Object value){
        return this.setCache(key, value, null, null);
    }

    /**
     * 设置缓存
     * @param key
     * @param value
     * @return
     */
    public boolean setCache(String key, Object value, Long expireTime, TimeUnit timeUnit){
        long startTime = System.nanoTime();
        if (expireTime != null && timeUnit != null){
            redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
        }else {
            redisTemplate.opsForValue().set(key, value);
        }
        log.info("[redis] set cache cost time: {} ns", System.nanoTime() - startTime);
        return Boolean.TRUE;
    }

    /**
     * 获取缓存
     * @param key
     * @param clz
     * @return
     * @param <T>
     */
    public <T> T getCache(String key, Class<T> clz){
        long startTime = System.nanoTime();
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null){
            return null;
        }
        log.info("[redis] get cache cost time: {} ns", System.nanoTime() - startTime);
        return JSON.parseObject(result.toString(), clz);
    }

}
