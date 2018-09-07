package com.terryrao.shiro.config;


import com.tx.sa.shiro.*;
import com.tx.sa.shiro.cache.RedisSessionConfig;
import com.tx.sa.shiro.cache.ShiroSessionDao;
import com.tx.sa.shiro.cache.local.EhCacheCacheConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * shiro 配置相关
 */
@Configuration
@ConditionalOnClass({SecurityManager.class,AuthorizingRealm.class})
@EnableConfigurationProperties(ShiroProperties.class)
@AutoConfigureAfter({EhCacheCacheConfig.class,RedisSessionConfig.class})
@Import(ShiroInterceptorConfig.class)
public class ShiroConfig {





    @Bean(name = "credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher getCredentialsMatcher() {
        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5"); //加密方式
        matcher.setHashIterations(2);//加密次数
        matcher.setStoredCredentialsHexEncoded(true); //十六进制编码
        return matcher;
    }


    @Bean(name = "adminRealm")
    @DependsOn(value = "lifecycleBeanPostProcessor")
    public Realm getAdminRealm(CredentialsMatcher credentialsMatcher) {
        AdminRealm realm = new AdminRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        realm.setCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");
        return realm;
    }

    /**
     * 会话ID生成器
     */
    @Bean(name = "sessionIdGenerator")
    public SessionIdGenerator getSessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    @Bean("sessionIdCookie")
    public SimpleCookie getSessionCookie() {
        SimpleCookie cookie = new SimpleCookie("sid");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }


    /**
     * rememberMe管理器
     */
  /*  @Bean("rememberMeManager")
    public CookieRememberMeManager getRememberManager(Cookie cookie,ShiroProperties shiroProperties) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCipherKey(Base64.decode(shiroProperties.getRememberSeed()));
        manager.setCookie(cookie);
        return manager;

          @Bean("sessionDAO")
    public EnterpriseCacheSessionDAO getSessionDao(SessionIdGenerator sessionIdGenerator) {
        EnterpriseCacheSessionDAO dao = new EnterpriseCacheSessionDAO();
        dao.setActiveSessionsCacheName("shiro-activeSessionCache");
        dao.setSessionIdGenerator(sessionIdGenerator);
        return dao;
    }
    }*/
    @Bean("sessionDAO")
    public SessionDAO getSessionDao(SessionIdGenerator sessionIdGenerator) {
        EnterpriseCacheSessionDAO dao = new EnterpriseCacheSessionDAO();
        dao.setActiveSessionsCacheName(ShiroSessionDao.SESSION_PREFIX);
        dao.setSessionIdGenerator(sessionIdGenerator);
        return dao;
    }


    @Bean("sessionManager")
    public SessionManager getSessionManager(SessionDAO sessionDAO, Cookie cookie) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionDAO(sessionDAO);
        sessionManager.setSessionIdCookie(cookie);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    @Bean("securityManager")
    public SecurityManager getSecurityManger(Realm realm, @Qualifier("sessionManager") SessionManager sessionManager
            , @Qualifier("shiroCacheManager") CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(cacheManager);
//        securityManager.setRememberMeManager(rememberMeManager);

        /*
        <!-- 相当于调用SecurityUtils.setSecurityManager(securityManager) -->
	<bean
		class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
		<property name="staticMethod"
			value="org.apache.shiro.SecurityUtils.setSecurityManager" />
		<property name="arguments" ref="securityManager" />
	</bean>
         */
        SecurityUtils.setSecurityManager(securityManager);
        return securityManager;
    }

    @Bean("sysUserFilter")
    public SysUserFilter getSysUserFilter() {
        return new SysUserFilter();
    }

    @Bean("authcFilter")
    public ShiroFormAuthenticationFilter getAuthenticationFilter() {
        ShiroFormAuthenticationFilter filter = new ShiroFormAuthenticationFilter();
        filter.setUsernameParam("username");
        filter.setPasswordParam("password");
        filter.setRememberMeParam("rememberMe");
        filter.setCaptchaParam("captcha");
        filter.setFailureKeyAttribute("shiroLoginFailure");
        return filter;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroFormAuthenticationFilter authenticationFilter) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager);
        bean.setLoginUrl("/login");
        bean.setUnauthorizedUrl("/login");
        Map<String, Filter> map = new LinkedHashMap<>();
        map.put("authc", authenticationFilter);
        bean.setFilters(map);
        Map<String, String> definitions = this.getFilterChainDefinitions();
        bean.setFilterChainDefinitionMap(definitions);
        return bean;

    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    private Map<String, String> getFilterChainDefinitions() {
        Map<String, String> filters = new LinkedHashMap<>();
        filters.put("/css/**", "anon");
        filters.put("/skin/**", "anon");
        filters.put("/images/**", "anon");
        filters.put("/api/**", "anon");
        filters.put("/js/**", "anon");
        filters.put("/lib/**", "anon");
        filters.put("/favicon.ico", "anon");
        filters.put("/txjcaptcha.svl", "anon");
        filters.put("/api/**", "anon");
        filters.put("/rmi/**", "anon");
        filters.put("/org/agreement/**", "anon");
        filters.put("/repayment/settle-advice*", "anon");
        filters.put("/repayment/mediacy-advice*", "anon");
        filters.put("/accFund/*Callback", "anon");
        filters.put("/org/*Callback", "anon");
        filters.put("/recharge/*Callback", "anon");
        filters.put("/depositeNotify/*", "anon");
        filters.put("/login", "authc");
        filters.put("/logout", "logout");
        filters.put("/file/upload", "user");
        filters.put("/**", "user");
        return filters;
    }

    @Bean
    public FilterRegistrationBean shiroFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new DelegatingFilterProxy());
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.setName("shiroFilter");
//        registration.setOrder(1);
        return registration;
    }



}
