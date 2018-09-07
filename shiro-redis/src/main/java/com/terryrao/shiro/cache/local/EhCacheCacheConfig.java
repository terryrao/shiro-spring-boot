package com.terryrao.shiro.cache.local;

import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 使用本地缓存
 */
@Configuration
// ShiroCache.java
@ConditionalOnProperty(name = "tr.shiro.cache", havingValue = "ehcache", matchIfMissing = true)
public class EhCacheCacheConfig {
    //ehcache
    @Bean(name = "ehCacheManager")
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean bean = new EhCacheManagerFactoryBean();
        bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:ehcache.xml"));
        bean.setShared(true);
        bean.afterPropertiesSet();
        return bean;
    }


    @Bean(name = "shiroCacheManager")
    public CacheManager getCacheManager(@Qualifier("ehCacheManager") EhCacheManagerFactoryBean ehcacheManager) {
        //spring 包装
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehcacheManager.getObject());

        SpringCacheManagerWrapper wrapper = new SpringCacheManagerWrapper();
        wrapper.setCacheManager(cacheManager);
        return wrapper;
    }


}
