package com.terryrao.shiro.cache;

import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 包装Spring cache抽象
 *
 * @author liuwb
 * @since 1.0
 * 2015 2015年7月3日
 */

public class ShiroRedisCache<K extends Serializable, V> implements Cache<K, V> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RedisTemplate<K, V> shiroRedisTemplate;
    private String prefix = "shiro_redis:";
    private final int expire = 1800; // s

    private ShiroRedisCache(RedisTemplate<K, V> shiroRedisTemplate) {
        this.shiroRedisTemplate = shiroRedisTemplate;
    }

    ShiroRedisCache(RedisTemplate<K, V> shiroRedisTemplate, String prefix) {
        this(shiroRedisTemplate);
        this.setPrefix(prefix);
    }

    @Override
    public V get(K key) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("get by Key: {}", key);
        }
        if (key == null) {
            return null;
        }
        return shiroRedisTemplate.opsForValue().get(getKey(key));
    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Key: {}, value: {}", key, value);
        }

        if (key == null || value == null) {
            return null;
        }

        shiroRedisTemplate.opsForValue().set(getKey(key), value, expire, TimeUnit.SECONDS);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        if (log.isDebugEnabled()) {
            log.debug("Key: {}", key);
        }

        if (key == null) {
            return null;
        }
        K preKey = getKey(key);
        V value = shiroRedisTemplate.opsForValue().get(preKey);
        shiroRedisTemplate.delete(preKey);
        return value;
    }

    @Override
    public void clear() throws CacheException {
        Set<K> keys = this.keys();
        shiroRedisTemplate.delete(keys);
    }

    @Override
    public int size() {
        Long len = shiroRedisTemplate.getConnectionFactory().getConnection().dbSize();
        return len.intValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        Set<K> set = shiroRedisTemplate.keys((K) ((prefix + "*")));
        Set<K> result = Sets.newHashSet();

        if (CollectionUtils.isEmpty(set)) {
            return Collections.emptySet();
        }

        result.addAll(set);
        return result;
    }

    @Override
    public Collection<V> values() {
        Set<K> keys = keys();
        List<V> values = new ArrayList<>(keys.size());
        for (K k : keys) {
            values.add(shiroRedisTemplate.opsForValue().get(getKey(k)));
        }
        return values;
    }

    private K getKey(K key) {
        if (key instanceof String) {
            String preKey = this.getPrefix() + key;
            return (K) preKey;
        } else {
            return key;
        }
    }

    private String getPrefix() {
        return prefix;
    }

    private void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
