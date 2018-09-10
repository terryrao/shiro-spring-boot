package com.terryrao.shiro.config;

import com.terry.admin.model.AdminLoginLog;
import com.terry.admin.model.AdminPermission;
import com.terry.admin.model.AdminUser;
import com.terry.admin.service.AdminLoginLogService;
import com.terry.admin.service.AdminPermissionService;
import com.terry.admin.service.AdminRoleService;
import com.terry.admin.service.AdminUserService;
import com.terryrao.shiro.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 本地 admin登录服务实现类
 */

@Service("adminLoginService")
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminLoginLogService adminLoginLogService;
    @Autowired
    private AdminPermissionService adminPermissionService;

    @Override
    public AdminUser findByName(String adminName) {
        return this.adminUserService.findByName(adminName);
    }

    @Override
    public List<AdminPermission> listPermissionsByRoleId(String roleId) {
        return this.adminPermissionService.listByRoleId(roleId);
    }


    @Override
    public boolean lockAdmin(String adminNo) {
        return this.adminUserService.lock(adminNo);
    }

   /* @Override
    public boolean saveLoginLog(ShiroUser user, String ip) {

        SysLog syslog = new SysLog();
        syslog.setCreateTime(DateUtils.today());
        syslog.setReqData("{'userName':'" + user.getUsername() + "'}");
        syslog.setLogTitle(SysLog.LOGIN_TITLE); // 操作描述
        syslog.setUserName(user.getUsername());
        syslog.setRealName(user.getRealName());
        syslog.setReqIp(ip);
        return this.sysLogServiceApi.saveLoginLog(syslog);
    }*/

    @Override
    public boolean saveSysLoginLog(AdminLoginLog sysLoginLog) {
        this.adminLoginLogService.add(sysLoginLog);
        return true;
    }
}
