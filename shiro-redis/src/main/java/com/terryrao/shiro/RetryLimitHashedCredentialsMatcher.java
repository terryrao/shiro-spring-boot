package com.terryrao.shiro;

import com.terry.admin.enums.UserStatus;
import com.terry.admin.model.AdminUser;
import com.terryrao.shiro.cache.local.EhcacheName;
import com.terryrao.shiro.constant.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static com.terryrao.shiro.constant.Constants.ERROR_LOGIN_LIMIT;

/**
 * 自定义shiro加密方式
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    private CacheManager cacheManager;
    @Autowired
    private AdminLoginService adminLoginService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        String key = Constants.ADMIN_ERRER_TIMES_ + DateTime.now().toString();
        //禁用用户
        AdminUser sysAdminInDb = adminLoginService.findByName(username);
        Cache<String, Integer> limitCache = getCacheManager().getCache(EhcacheName.PASSWORD_RETRY_CACHE.getCacheKey());
        Integer count = limitCache.get(key);
        String adminNo = sysAdminInDb.getAdminNo();
        if (ERROR_LOGIN_LIMIT > 0) {
            if (count + 1 > ERROR_LOGIN_LIMIT) {
                AdminUser admin = new AdminUser();
                admin.setAdminNo(sysAdminInDb.getAdminNo());
                admin.setUpdateTime(new Date());
                admin.setStatus(UserStatus.SD);
                adminLoginService.lockAdmin(adminNo);
                throw new ExcessiveAttemptsException(String.format("您的帐号当日已登录错误超过%s次，账号已锁定，请联系管理员进行帐号解锁操作", ERROR_LOGIN_LIMIT));
            }
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String password = new String(usernamePasswordToken.getPassword());// 输入密码
        Object accountCredentials = info.getCredentials(); // 数据库密码
        password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));// 加密
        boolean matches = equals(password, accountCredentials);
        if (matches) {
            limitCache.remove(key);
        } else {
            try {
                limitCache.put(key + adminNo, count + 1);
            } catch (Exception e) {
                logger.error("后台登录redis set key异常", e);
            }
        }
        return matches;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
