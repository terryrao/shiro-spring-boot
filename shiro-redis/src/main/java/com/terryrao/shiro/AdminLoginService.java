package com.terryrao.shiro;


import com.terryrao.admin.model.AdminLoginLog;
import com.terryrao.admin.model.AdminPermission;
import com.terryrao.admin.model.AdminUser;

import java.util.List;

/**
 * 登录服务  所有使用 shiro 验证系统的请实现此类
 * 2018/4/24
 *
 */
public interface AdminLoginService {

    /**
     * 使用登录名称获取用户信息
     */
    AdminUser findByName(String adminName);

    /**
     * 获取所有的权限
     */
    List<AdminPermission> listPermissionsByRoleId(String roleId);


    /**
     * 禁用用户
     */
    boolean lockAdmin(String adminNo);

//
//    /**
//     * 登录日志
//     */
//    boolean saveLoginLog(ShiroUser user, String ip);

    /**
     * 系统登录日志
     */
    boolean saveSysLoginLog(AdminLoginLog loginLog);


}
