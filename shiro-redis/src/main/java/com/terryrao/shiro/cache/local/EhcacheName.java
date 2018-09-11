package com.terryrao.shiro.cache.local;

import lombok.Getter;

@Getter
public enum EhcacheName {
    SHIRO_USER_MENUS("shiro-user-menus"),
    SHIRO_USER("shiro-user"),
    SHIRO_ACTIVE_SESSION_CACHE("shiro-active-session-cache"),
    AUTHENTICATION_CACHE("authentication-cache"),
    AUTHORIZATION_CACHE("authorization-cache"),
    PASSWORD_RETRY_CACHE("password-retry-cache");
    private String cacheKey;

    EhcacheName(String cacheKey) {
        this.cacheKey = cacheKey;
    }


}
