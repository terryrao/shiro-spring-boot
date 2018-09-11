package com.terry.admin.service;

import com.terry.admin.basic.BasicPageService;
import com.terry.admin.basic.BasicService;
import com.terry.admin.model.AdminPermission;
import com.terry.admin.vo.MenuTree;

import java.util.List;

public interface AdminPermissionService extends BasicService<AdminPermission>,BasicPageService<AdminPermission> {

    List<AdminPermission> listByRoleId(String roleId);
    MenuTree listAllByRoleId(String roleId);


}
