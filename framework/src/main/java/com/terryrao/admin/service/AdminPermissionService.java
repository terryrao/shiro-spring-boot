package com.terryrao.admin.service;

import com.terryrao.admin.basic.BasicPageService;
import com.terryrao.admin.basic.BasicService;
import com.terryrao.admin.model.AdminPermission;
import com.terryrao.admin.vo.MenuTree;

import java.util.List;

public interface AdminPermissionService extends BasicService<AdminPermission>,BasicPageService<AdminPermission> {

    List<AdminPermission> listByRoleId(String roleId);
    MenuTree listAllByRoleId(String roleId);


}
