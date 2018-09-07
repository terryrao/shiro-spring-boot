package com.terryrao.shiro;




import com.terry.admin.model.AdminUser;

import java.util.List;

/**
 * 登录服务  所有使用 shiro 验证系统的请实现此类
 * 2018/4/24
 *
 * @author raowei
 */
public interface AdminLoginService {

    /**
     * 使用登录名称获取用户信息
     */
    AdminUser findAdminRoleVoByName(String adminName);

    /**
     * 获取所有的权限
     */
    List<SysPermission> findSysRolePermissionByRoleNo(String roleId);


    /**
     * 使用登录名称获取用户信息
     */
    SysAdmin findSysAdminByName(String adminName);


    /**
     * 禁用用户
     */
    boolean lockAdmin(String adminNo);


    /**
     * 登录日志
     */
    boolean saveLoginLog(ShiroUser user, String ip);

    /**
     * 系统登录日志
     */
    boolean saveSysLoginLog(SysLoginLog sysLoginLog);


}
