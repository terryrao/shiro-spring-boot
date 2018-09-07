package com.terry.admin.model;

import lombok.Data;

@Data
public class AdminRolePermission {
    private int id;
    private String roleNo; // 角色编号
    private String permissionNo; // 菜单编号
}
