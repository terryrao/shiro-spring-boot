package com.terryrao.controller;


import com.octo.captcha.CaptchaException;
import com.terryrao.admin.basic.BasicController;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.terryrao.shiro.constant.Constants.ERROR_LOGIN_LIMIT;

@Controller
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LoginController extends BasicController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 登录
     */
    @RequestMapping(value = "/login")
    public String showLoginForm(HttpServletRequest req) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/index";
        }
        String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
        String error = null;
        if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名/密码错误";
        } else if (LockedAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户被锁定";
        } else if (CaptchaException.class.getName().equals(exceptionClassName)) {
            error = "验证码错误";
        } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
            error = String.format("已登录错误超过%s次，账号已锁定", ERROR_LOGIN_LIMIT);
        } else if (exceptionClassName != null) {
            error = "登录失败";

        }
        if (StringUtils.isNotBlank(error)) {
            logger.error("登陆失败：" + error + "【" + exceptionClassName + "】");
        }
        req.setAttribute("error", error);
        return "login";
    }

    /**
     * 无权限
     */
    @RequestMapping(value = "/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

}
