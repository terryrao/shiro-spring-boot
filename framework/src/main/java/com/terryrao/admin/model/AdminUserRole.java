package com.terryrao.admin.model;

import lombok.Data;

@Data
public class AdminUserRole {
    private Long id; // 自增主键
    private String roleNo; // 角色编号
    private String adminNo; // 用户编号
}
