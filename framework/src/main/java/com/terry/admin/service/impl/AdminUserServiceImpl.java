package com.terry.admin.service.impl;

import com.github.pagehelper.Page;
import com.terry.admin.dao.AdminUserDao;
import com.terry.admin.enums.UserStatus;
import com.terry.admin.model.AdminRole;
import com.terry.admin.model.AdminUser;
import com.terry.admin.service.AdminUserService;
import com.terry.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserDao adminUserDao;

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
    public int delete(AdminUser del) {
        return this.adminUserDao.delete(del);
    }

    @Override
    public Page<AdminUser> listByPage(AdminUser condition, Page<AdminUser> page) {
        return PageUtils.getLocalPage(page,this.adminUserDao::list,condition);
    }
}
