package com.terryrao.admin.service;

import com.terryrao.admin.basic.BasicPageService;
import com.terryrao.admin.basic.BasicService;
import com.terryrao.admin.model.AdminUser;

public interface AdminUserService extends BasicService<AdminUser>,BasicPageService<AdminUser> {

    /**
     *
     * @param adminName 用户登录名
     */
    AdminUser findByName(String adminName);

    /**
     * 锁定用户
     * @param adminNO 用户ID
     */
    boolean lock(String adminNO);



}
