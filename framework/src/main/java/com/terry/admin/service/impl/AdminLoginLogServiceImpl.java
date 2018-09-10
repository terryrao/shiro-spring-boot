package com.terry.admin.service.impl;

import com.github.pagehelper.Page;
import com.terry.admin.dao.AdminLoginLogDao;
import com.terry.admin.model.AdminLoginLog;
import com.terry.admin.service.AdminLoginLogService;
import com.terry.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminLoginLogService")
public class AdminLoginLogServiceImpl implements AdminLoginLogService {
    @Autowired
    private AdminLoginLogDao adminLoginLogDao;

    @Override
    public void add(AdminLoginLog entity) {
        this.adminLoginLogDao.add(entity);
    }

    @Override
    public List<AdminLoginLog> list(AdminLoginLog condition) {
        return this.adminLoginLogDao.list(condition);
    }

    @Override
    public int update(AdminLoginLog upd) {
        return this.adminLoginLogDao.update(upd);
    }

    @Override
    public int delete(AdminLoginLog del) {
        return this.adminLoginLogDao.delete(del);
    }

    @Override
    public Page<AdminLoginLog> listByPage(AdminLoginLog condition, Page<AdminLoginLog> page) {
        return PageUtils.getLocalPage(page, this.adminLoginLogDao::list, condition);
    }
}
