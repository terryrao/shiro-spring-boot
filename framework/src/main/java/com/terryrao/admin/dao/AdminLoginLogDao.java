package com.terryrao.admin.dao;

import com.terryrao.admin.basic.BasicMapper;
import com.terryrao.admin.model.AdminLoginLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminLoginLogDao extends BasicMapper<AdminLoginLog> {
}
