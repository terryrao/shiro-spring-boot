package com.terryrao.admin.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
public class AdminLoginLog {
    private Long id; // 自增ID
    private String adminNo; // 用户编号
    private String adminName; // 登录账号
    private Date loginTime; // 登录时间
    private String loginIp; // 登录IP
    private String loginAddr; // 登录地址
    private String loginDevice; // 登录设备
    private String loginBrowser; // 登录浏览器
    private String source; // 登录来源,1:PC,2:微站,3:APP,4:后台添加
    private String remark; // 备注
}
