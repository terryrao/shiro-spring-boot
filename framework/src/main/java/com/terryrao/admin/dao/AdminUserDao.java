package com.terryrao.admin.dao;

import com.terryrao.admin.basic.BasicMapper;
import com.terryrao.admin.model.AdminUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserDao extends BasicMapper<AdminUser> {


    AdminUser findByName(String username);

}
