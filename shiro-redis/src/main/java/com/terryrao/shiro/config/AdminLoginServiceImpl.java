package com.terryrao.shiro.config;

import com.terryrao.shiro.AdminLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 本地 admin登录服务实现类
 */

@Service("AdminLoginService")
public class AdminLoginServiceImpl implements AdminLoginService {
    @Autowired
    private AdminServiceApi adminServiceApi;
    @Autowired
    private RoleServiceApi roleServiceApi;
    @Autowired
    private SysLoginLogServiceApi sysLoginLogServiceApi;
    @Autowired
    private SysLogServiceApi sysLogServiceApi;

    @Override
    public AdminRoleVo findAdminRoleVoByName(String adminName) {
        return this.adminServiceApi.findAdminRoleVoByName(adminName);
    }

    @Override
    public List<SysPermission> findSysRolePermissionByRoleNo(String roleId) {
        return this.roleServiceApi.findSysRolePermissionByRoleNo(roleId);
    }

    @Override
    public SysAdmin findSysAdminByName(String adminName) {
        return adminServiceApi.findSysAdminByName(adminName);
    }

    @Override
    public boolean lockAdmin(String adminNo) {
        return adminServiceApi.lockAdmin(adminNo);
    }

    @Override
    public boolean saveLoginLog(ShiroUser user, String ip) {

        SysLog syslog = new SysLog();
        syslog.setCreateTime(DateUtils.today());
        syslog.setReqData("{'userName':'" + user.getUsername() + "'}");
        syslog.setLogTitle(SysLog.LOGIN_TITLE); // 操作描述
        syslog.setUserName(user.getUsername());
        syslog.setRealName(user.getRealName());
        syslog.setReqIp(ip);
        return this.sysLogServiceApi.saveLoginLog(syslog);
    }

    @Override
    public boolean saveSysLoginLog(SysLoginLog sysLoginLog) {
        return this.sysLoginLogServiceApi.save(sysLoginLog);
    }
}
