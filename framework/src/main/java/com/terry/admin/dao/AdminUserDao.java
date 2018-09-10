package com.terry.admin.dao;

import com.terry.admin.basic.BasicMapper;
import com.terry.admin.model.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserDao extends BasicMapper<AdminUser> {


    AdminUser findByName(String username);

}
