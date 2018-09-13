-- create database  roow;
-- use roow;

CREATE TABLE `admin_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `admin_no` char(32) NOT NULL COMMENT 'admin编号',
  `name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `real_name` varchar(32) NOT NULL COMMENT '真实姓名',
  `phone` varchar(12) NOT NULL COMMENT '手机号码',
  `email` varchar(32) NOT NULL COMMENT '邮箱',
  `status` enum('JY','QY','SD') NOT NULL DEFAULT 'QY' COMMENT '状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `sys_admin_no` (`admin_no`)
) ENGINE=InnoDB AUTO_INCREMENT=1161 DEFAULT CHARSET=utf8 COMMENT='后台用户表';

CREATE TABLE `admin_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_no` char(32) NOT NULL COMMENT '角色编号',
  `role_name` varchar(32) NOT NULL,
  `org` varchar(50) DEFAULT NULL COMMENT '所属机构',
  `role_descn` varchar(255) NOT NULL COMMENT '角色描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_role_no` (`role_no`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='角色表';

CREATE TABLE `admin_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_no` char(32) NOT NULL COMMENT '菜单编号',
  `name` varchar(100) NOT NULL COMMENT '菜单名称',
  `code` varchar(100) NOT NULL COMMENT '菜单代码',
  `url` varchar(200) DEFAULT NULL COMMENT '地址',
  `status` enum('JY','QY') DEFAULT NULL COMMENT '状态',
  `parent_id` char(32) DEFAULT NULL COMMENT '父级ID',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `type` enum('MENU','BUTTON') DEFAULT 'MENU' COMMENT '菜单类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_sys_permission` (`permission_no`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=680 DEFAULT CHARSET=utf8 COMMENT='菜单表';


CREATE TABLE `admin_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `role_no` char(32) NOT NULL COMMENT '角色编号',
  `admin_no` char(32) NOT NULL COMMENT '用户编号',
  PRIMARY KEY (`id`),
  KEY `idx_role_no` (`role_no`),
  KEY `idx_admin_no` (`admin_no`)
) ENGINE=InnoDB AUTO_INCREMENT=617 DEFAULT CHARSET=utf8 COMMENT='用户角色表';


CREATE TABLE `admin_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `role_no` char(32) NOT NULL COMMENT '角色编号',
  `permission_no` char(32) NOT NULL COMMENT '菜单编号',
  PRIMARY KEY (`id`),
  KEY `idx_role_permission` (`role_no`),
  KEY `idx_permission_no` (`permission_no`)
) ENGINE=InnoDB AUTO_INCREMENT=62685 DEFAULT CHARSET=utf8 COMMENT='角色权限表';


CREATE TABLE `admin_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `admin_no` char(32) DEFAULT NULL COMMENT '用户编号',
  `admin_name` varchar(32) DEFAULT NULL COMMENT '登录账号',
  `login_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  `login_ip` varchar(16) DEFAULT NULL COMMENT '登录IP',
  `login_addr` varchar(64) DEFAULT NULL COMMENT '登录地址',
  `login_device` varchar(64) DEFAULT NULL COMMENT '登录设备',
  `login_browser` varchar(64) DEFAULT NULL COMMENT '登录浏览器',
  `source` char(1) DEFAULT NULL COMMENT '登录来源,1:PC,2:微站,3:APP,4:后台添加',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88956 DEFAULT CHARSET=utf8 COMMENT='登录日志表';