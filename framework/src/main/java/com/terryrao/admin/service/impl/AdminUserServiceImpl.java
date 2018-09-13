package com.terryrao.admin.service.impl;

import com.github.pagehelper.Page;
import com.terryrao.admin.dao.AdminUserDao;
import com.terryrao.admin.enums.UserStatus;
import com.terryrao.admin.model.AdminUser;
import com.terryrao.admin.service.AdminUserService;
import com.terryrao.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
    private final AdminUserDao adminUserDao;

    @Autowired
    public AdminUserServiceImpl(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Override
    public AdminUser findByName(String adminName) {
        return adminUserDao.findByName(adminName);
    }

    @Override
    public boolean lock(String adminNO) {
        AdminUser upd = new AdminUser();
        upd.setAdminNo(adminNO);
        upd.setStatus(UserStatus.JY);
        return this.adminUserDao.update(upd) > 0;
    }

    @Override
    public void add(AdminUser entity) {
        this.adminUserDao.add(entity);
    }

    @Override
    public List<AdminUser> list(AdminUser condition) {
        return this.adminUserDao.list(condition);
    }

    @Override
    public int update(AdminUser upd) {
        return this.adminUserDao.update(upd);
    }

    @Override
    public int deleteByPrimaryKey(String id) {
        return this.adminUserDao.deleteByPrimaryKey(id);
    }

    @Override
    public AdminUser selectByPrimaryKey(String key) {
        return this.adminUserDao.selectByPrimaryKey(key);
    }

    @Override
    public Page<AdminUser> listByPage(AdminUser condition, Page<AdminUser> page) {
        return PageUtils.getLocalPage(page, this.adminUserDao::list, condition);
    }
}
