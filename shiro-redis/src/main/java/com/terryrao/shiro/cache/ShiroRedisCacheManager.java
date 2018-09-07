package com.terryrao.shiro.cache;

import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * CacheManager
 */
public class ShiroRedisCacheManager extends AbstractCacheManager {
    private RedisTemplate<Serializable, Object> shiroRedisTemplate;

    public ShiroRedisCacheManager(RedisTemplate<Serializable, Object> shiroRedisTemplate) {
        this.shiroRedisTemplate = shiroRedisTemplate;
    }

    @Override
    public Cache<Serializable, Object> createCache(String name) throws CacheException {
        return new ShiroRedisCache<>(shiroRedisTemplate, name);
    }
}
