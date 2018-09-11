package com.terryrao.shiro;

import com.terryrao.shiro.cache.local.EhcacheName;
import com.terryrao.shiro.constant.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * 权限拦截器
 * 2015 2015年7月3日
 *
 * @author liuwenbin
 * @since 1.0
 */
public class ShiroUserInterceptor extends HandlerInterceptorAdapter {

    private String[] allowUrls = {"/login", "/index", "/welcome", "/logout", "/unauthorized"};

    private CacheManager cacheManager;

    private boolean flag = true;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (flag) {
            return true;
        }
        // 取访问路径
        String requestUrl = request.getRequestURI();
        for (String url : allowUrls) {
            if (requestUrl.contains(url)) {
                return true;
            }
        }
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        if (shiroUser == null) {
            return super.preHandle(request, response, handler);
        }
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());
        Cache<String, Set<String>> menusCache = getCacheManager().getCache(EhcacheName.SHIRO_USER_MENUS.getCacheKey());
        Set<String> set = menusCache.get(shiroUser.getAdminNo() +
                Constants.CURRENT_USER_MENUS);
        boolean flag = set != null && set.contains(url);
        if (!flag) {// 没有权限访问
            request.getRequestDispatcher("/unauthorized").forward(request, response);
            return false;
        }
        return super.preHandle(request, response, handler);
    }

    private CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
