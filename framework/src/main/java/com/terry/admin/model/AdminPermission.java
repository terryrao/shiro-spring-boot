package com.terry.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter@Getter@NoArgsConstructor
public class AdminPermission {
    private Long id; //
    private String permissionNo; // 菜单编号
    private String name; // 菜单名称
    private String code; // 菜单代码
    private String url; // 地址
    private String parentId; // 父级ID
    private Date createTime; //
    private Date updateTime; //
    private String status;
    private String type;//类型MENU BUTTON 按钮
    private String sort;//排序
}
