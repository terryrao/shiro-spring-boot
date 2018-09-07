package com.terryrao.shiro.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * Created by terryrao on 2018/4/26.
 */
@Configuration
@EnableConfigurationProperties(ShiroSessionRedisProperties.class)
// ShiroCache.java
@ConditionalOnProperty(name = "tx.shiro.cache", havingValue = "redis")
public class RedisSessionConfig {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * Redis连接客户端(Session及Shiro缓存管理)
     */
    @Primary
    @Bean(name = "connectionFactory")
    public RedisConnectionFactory connectionFactory(ShiroSessionRedisProperties myRedisProperties) {
        JedisConnectionFactory conn = new JedisConnectionFactory();
        conn.setDatabase(myRedisProperties.getDatabase());
        conn.setHostName(myRedisProperties.getHost());
        conn.setPassword(myRedisProperties.getPassword());
        conn.setPort(myRedisProperties.getPort());
        conn.setTimeout(myRedisProperties.getTimeout());
        log.info("1.初始化Redis缓存服务器(登录用户Session及Shiro缓存管理)... ...");
        return conn;
    }


    @Bean(name = "shiroRedisTemplate")
    public RedisTemplate<Serializable, Object> shiroRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Serializable, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean(name = "shiroCacheManager")
    @DependsOn(value = "shiroRedisTemplate")
    public ShiroRedisCacheManager redisCacheManager(RedisTemplate<Serializable, Object> redisTemplate) {
        return new ShiroRedisCacheManager(redisTemplate);

    }


}
