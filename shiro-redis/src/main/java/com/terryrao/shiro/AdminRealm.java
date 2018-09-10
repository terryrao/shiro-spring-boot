package com.terryrao.shiro;


import com.google.common.collect.Sets;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.terry.admin.enums.UserStatus;
import com.terry.admin.model.AdminPermission;
import com.terry.admin.model.AdminUser;
import com.terryrao.shiro.constant.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 后台用户认证类
 * 2015 2015年6月26日
 *
 * @author liuwenbin
 * @since 1.0
 */
public class AdminRealm extends AuthorizingRealm {

    @Autowired
    private AdminLoginService adminLoginService;


    @Autowired
    private ImageCaptchaService captchaService;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //查询用户权限
        AdminUser adminUser = adminLoginService.findByName(shiroUser.getUsername());
        Set<String> roleSet = adminUser.getRoleIdSet();
        authorizationInfo.setRoles(roleSet);//设置角色
        List<AdminPermission> permissions = adminLoginService.listPermissionsByRoleId(adminUser.getRoleId());//查询权限拥有的菜单
        Cache<String, Set<String>> menusCache = getCacheManager().getCache("shiro-user-menus");
        menusCache.put(adminUser.getAdminNo() + Constants.CURRENT_USER_MENUS, getMenuSet(permissions, "menus"));//放入缓存
        authorizationInfo.setStringPermissions(getMenuSet(permissions, "permissions"));//设置菜单权限
        return authorizationInfo;
    }

    private Set<String> getMenuSet(List<AdminPermission> permissions, String type) {
        Set<String> set = Sets.newHashSet();
        if (permissions == null || permissions.size() <= 0) {
            return Collections.emptySet();
        }
        if ("permissions".equals(type)) {// 获取权限编码set
            permissions.forEach(permission -> set.add(permission.getCode()));
        } else {// 获取菜单地址set
            permissions.forEach(permission -> set.add(permission.getUrl()));
        }
        return set;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
        String username = (String) token.getPrincipal();
            String captcha = token.getCaptcha(); // dev、test不需要验证，uat、prod才需要验证码
            boolean flag = captchaService.validateResponseForID(SecurityUtils.getSubject().getSession().getId() + "", captcha.toLowerCase());//验证码
            if (!flag) {
                throw new CaptchaException("验证码错误"); // 验证码错误
            }
        AdminUser adminUser= adminLoginService.findByName(username);
        if (adminUser == null) {
            throw new UnknownAccountException();// 没找到帐号
        }

        if (UserStatus.JY.equals(adminUser.getStatus()) || UserStatus.SD.equals(adminUser.getStatus())) {
            throw new LockedAccountException(); // 帐号锁定
        }
        ShiroUser shiroUser = new ShiroUser(adminUser);
        String password = new String(token.getPassword());
        shiroUser.setSimple(PasswordHelper.checkPasswordIsSimple(password, username));
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(shiroUser, adminUser.getPassword(), getName()
        );
        //缓存
        try {
            Cache<String, AdminUser> userCache = getCacheManager().getCache("shiro-user");
            userCache.put(adminUser.getAdminNo() + Constants.CURRENT_USER, adminUser);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return authenticationInfo;
    }


    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        cleanMenuCache(principals);
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        cleanAdminCache(principals);
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

    /**
     * 清除目录缓存
     */
    public void cleanMenuCache(PrincipalCollection principals) {
        Cache<String, Set<String>> menusCache = getCacheManager().getCache("shiro-user-menus");
        ShiroUser shiro = (ShiroUser) principals.getPrimaryPrincipal();
        menusCache.remove(shiro.getAdminNo() + Constants.CURRENT_USER_MENUS);
    }

    /**
     * 清除用户信息缓存
     */
    public void cleanAdminCache(PrincipalCollection principals) {
        ShiroUser shiro = (ShiroUser) principals.getPrimaryPrincipal();
        Cache<String, AdminUser> userCache = getCacheManager().getCache("shiro-user");
        userCache.remove(shiro.getAdminNo() + Constants.CURRENT_USER);

    }

    private static final String OR_OPERATOR = " or ";
    private static final String AND_OPERATOR = " and ";
    private static final String NOT_OPERATOR = "not ";

    /**
     * 支持or and not 关键词  不支持and or混用
     */
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        if (permission.contains(OR_OPERATOR)) {
            String[] permissions = permission.split(OR_OPERATOR);
            for (String orPermission : permissions) {
                if (isPermittedWithNotOperator(principals, orPermission)) {
                    return true;
                }
            }
            return false;
        } else if (permission.contains(AND_OPERATOR)) {
            String[] permissions = permission.split(AND_OPERATOR);
            for (String orPermission : permissions) {
                if (!isPermittedWithNotOperator(principals, orPermission)) {
                    return false;
                }
            }
            return true;
        } else {
            return isPermittedWithNotOperator(principals, permission);
        }
    }

    private boolean isPermittedWithNotOperator(PrincipalCollection principals, String permission) {
        if (permission.startsWith(NOT_OPERATOR)) {
            return !super.isPermitted(principals, permission.substring(NOT_OPERATOR.length()));
        } else {
            return super.isPermitted(principals, permission);
        }
    }
}
