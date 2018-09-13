package com.terryrao.admin.service.impl;

import com.github.pagehelper.Page;
import com.terryrao.admin.dao.AdminPermissionDao;
import com.terryrao.admin.model.AdminPermission;
import com.terryrao.admin.service.AdminPermissionService;
import com.terryrao.admin.util.PageUtils;
import com.terryrao.admin.vo.MenuTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminPermissionService")
public class AdminPermissionServiceImpl implements AdminPermissionService {
    private final AdminPermissionDao adminPermissionDao;

    @Autowired
    public AdminPermissionServiceImpl(AdminPermissionDao adminPermissionDao) {
        this.adminPermissionDao = adminPermissionDao;
    }

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
    public int deleteByPrimaryKey(String delId) {
        return this.adminPermissionDao.deleteByPrimaryKey(delId);
    }

    @Override
    public AdminPermission selectByPrimaryKey(String key) {
        return this.adminPermissionDao.selectByPrimaryKey(key);
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

    @Override
    public MenuTree listAllByRoleId(String roleId) {
        List<AdminPermission> list = this.listByRoleId(roleId);
        MenuTree menuTree = new MenuTree();
        return menuTree.createTree(list);
    }
}
