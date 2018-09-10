package com.terry.admin.service.impl;

import com.github.pagehelper.Page;
import com.terry.admin.dao.AdminPermissionDao;
import com.terry.admin.model.AdminPermission;
import com.terry.admin.service.AdminPermissionService;
import com.terry.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminPermissionService")
public class AdminPermissionServiceImpl implements AdminPermissionService {
    @Autowired
    private AdminPermissionDao adminPermissionDao;
    @Override
    public void add(AdminPermission entity) {
        this.adminPermissionDao.add(entity);
    }

    @Override
    public List<AdminPermission> list(AdminPermission condition) {
        return this.adminPermissionDao.list(condition);
    }

    @Override
    public int update(AdminPermission upd) {
        return this.adminPermissionDao.update(upd);
    }

    @Override
    public int delete(AdminPermission del) {
        return this.adminPermissionDao.delete(del);
    }

    @Override
    public Page<AdminPermission> listByPage(AdminPermission condition, Page<AdminPermission> page) {
        return PageUtils.getLocalPage(page,this.adminPermissionDao::list,condition);
    }

    @Override
    public List<AdminPermission> listByRoleId(String roleId) {
        AdminPermission condition = new AdminPermission();
        condition.setRoleId(roleId);
        return this.adminPermissionDao.list(condition);
    }
}
