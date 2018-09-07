package com.terryrao.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 自定义shiro加密方式
 *
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {


    @Autowired
    private AdminLoginService adminLoginService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        String key = Constant.ADMIN_ERRER_TIMES_ + DateUtils.currentDate();
        //禁用用户
        SysAdmin sysAdminInDb = adminLoginService.findSysAdminByName(username);
        String adminNo = sysAdminInDb.getAdminNo();
        int count = 0;
        try {
            if (redisService.exists(key + adminNo)) {
                count = redisService.getValue(key + adminNo);
            }
        } catch (Exception e) {
            logger.error("后台登录redis获取异常", e);
        }
        int loginErrorCount = GeneralParameter.getInt("ADMIN_LOGIN_ERROR_COUNT", 0);
        if (loginErrorCount > 0) {
            if (count + 1 > loginErrorCount) {
                SysAdmin admin = new SysAdmin();
                admin.setAdminNo(sysAdminInDb.getAdminNo());
                admin.setUpdateTime(new Date());
                admin.setStatus(IsUsable.SD);
                adminLoginService.lockAdmin(adminNo);
                throw new ExcessiveAttemptsException(String.format("您的帐号当日已登录错误超过%s次，账号已锁定，请联系管理员进行帐号解锁操作", loginErrorCount));
            }
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String password = new String(usernamePasswordToken.getPassword());// 输入密码
        Object accountCredentials = info.getCredentials(); // 数据库密码
        password = UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));// 加密
        boolean matches = equals(password, accountCredentials);
        if (matches) {
            try {
                if (redisService.exists(key + adminNo))
                    redisService.remove(key + adminNo);
            } catch (RedisException e) {
                logger.error("后台登录redis remove key异常", e);
            } catch (Exception e) {
                logger.error("后台登录redis remove key异常", e);
                e.printStackTrace();
            }
        } else {
            try {
                redisService.setValue(key + adminNo, count + 1, 24 * 60 * 60);
            } catch (Exception e) {
                logger.error("后台登录redis set key异常", e);
            }
        }
        return matches;
    }
}
