package com.terryrao.controller;

import com.terryrao.admin.basic.BasicController;
import com.terryrao.admin.service.AdminPermissionService;
import com.terryrao.admin.vo.MenuTree;
import com.terryrao.shiro.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IndexController extends BasicController {

    private final AdminPermissionService permissionService;

    @Autowired
    public IndexController(AdminPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @RequestMapping(value = {"/"}, method = {RequestMethod.GET})
    public String toIdx() {
        return "index";
    }

    @RequestMapping(value = {"/index"}, method = {RequestMethod.GET})
    public String doIndex(Model model) {
        try {
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            String roleId = shiroUser.getRoleId() == null ? "" : shiroUser.getRoleId();
            MenuTree tree = permissionService.listAllByRoleId(roleId);
            model.addAttribute("shiro", shiroUser);
            model.addAttribute("tree", tree);
            model.addAttribute("simple", shiroUser.isSimple() ? 1 : 0);
            shiroUser.setSimple(false);
        } catch (Exception e) {
            logger.error("获取菜单列表", e);
            return this.pageNotFound();
        }
        return "index";
    }

    /**
     * 定位错误页面
     */
    @RequestMapping(value = "/error/{errorCode}")
    public String error(@PathVariable String errorCode) {
        return "p_error/" + errorCode;
    }

    @RequestMapping(value = "/welcome", method = {RequestMethod.GET})
    public String doWelcome() {
        return "welcome";
    }

}
