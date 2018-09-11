package com.terryrao.shiro;

import com.terry.admin.util.SpringUtils;
import org.apache.shiro.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * shiro 拦截器
 */
public class ShiroInterceptorConfig implements WebMvcConfigurer {
    @Bean   //把我们的拦截器注入为bean
    public HandlerInterceptor getMyInterceptor() {
        CacheManager cacheManager = (CacheManager) SpringUtils.getBean("shiroCacheManager");
        ShiroUserInterceptor shiroUserInterceptor = new ShiroUserInterceptor();
        shiroUserInterceptor.setCacheManager(cacheManager);
        return shiroUserInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则, 这里假设拦截 /url 后面的全部链接
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(getMyInterceptor()).addPathPatterns("/**");
    }
}
