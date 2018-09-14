package com.terryrao.controller;

import com.github.pagehelper.Page;
import com.terryrao.admin.basic.BasicController;
import com.terryrao.admin.model.AdminPermission;
import com.terryrao.admin.service.AdminPermissionService;
import com.terryrao.admin.service.AdminRoleService;
import com.terryrao.admin.service.AdminUserService;
import com.terryrao.admin.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminController extends BasicController {

    @Autowired
    private AdminPermissionService adminPermissionService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;


    @RequestMapping("/menu/list")
    public String listPermission(String pageSize, String pageNo, AdminPermission adminPermission, Model model) {
        Page<AdminPermission> page = PageUtils.newPage(pageNo, pageSize);
        page = this.adminPermissionService.listByPage(adminPermission, page);
        model.addAttribute("page",page);
        return "/admin/permission";
    }


}
