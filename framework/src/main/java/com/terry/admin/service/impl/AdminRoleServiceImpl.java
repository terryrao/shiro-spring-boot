package com.terry.admin.service.impl;

import com.github.pagehelper.Page;
import com.terry.admin.dao.AdminRoleDao;
import com.terry.admin.model.AdminRole;
import com.terry.admin.service.AdminRoleService;
import com.terry.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminRoleService")
public class AdminRoleServiceImpl implements AdminRoleService {
    @Autowired
    private AdminRoleDao adminRoleDao;


    @Override
    public void add(AdminRole entity) {
        this.adminRoleDao.add(entity);
    }

    @Override
    public List<AdminRole> list(AdminRole condition) {
        return this.adminRoleDao.list(condition);
    }

    @Override
    public int update(AdminRole upd) {
        return this.adminRoleDao.update(upd);
    }

    @Override
    public int delete(AdminRole del) {
        return this.adminRoleDao.delete(del);
    }

    @Override
    public Page<AdminRole> listByPage(AdminRole condition, Page<AdminRole> page) {
        return PageUtils.getLocalPage(page,this.adminRoleDao::list,condition);
    }
}
