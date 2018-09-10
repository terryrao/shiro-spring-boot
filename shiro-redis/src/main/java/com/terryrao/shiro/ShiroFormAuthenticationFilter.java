package com.terryrao.shiro;

import com.terry.admin.model.AdminLoginLog;
import com.terry.admin.util.IpUtils;
import com.terryrao.shiro.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * shiro表单过滤器扩展(增加验证码、记录日志)
 *
 */
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private AdminLoginService adminLoginService;

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        if (request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
        return super.onAccessDenied(request, response, mappedValue);
    }

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;

    public void setCaptchaParam(String captchaParam) {
        this.captchaParam = captchaParam;
    }

    public String getCaptchaParam() {

        return captchaParam;

    }

    protected String getCaptcha(ServletRequest request) {

        return WebUtils.getCleanParam(request, getCaptchaParam());

    }

    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        password = StringUtils.isBlank(password) ? "" : password;
        String captcha = getCaptcha(request);
        captcha = StringUtils.isBlank(captcha) ? "" : captcha;
        String host = getHost(request);
        return new UsernamePasswordCaptchaToken(username, password.toCharArray(), host, captcha);
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest servletRequest,
                                     ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession(false);
        ShiroUser shiro = (ShiroUser) subject.getPrincipal();
        session.setAttribute(Constants.CURRENT_USER, shiro);
        this.addLog(request, shiro);
        WebUtils.issueRedirect(request, servletResponse, getSuccessUrl());

        return false;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response) {
        logger.error("登录失败:" + e.getMessage(), e);// 登录失败日志
        return super.onLoginFailure(token, e, request, response);
    }

    private void addLog(HttpServletRequest request, ShiroUser shiro) {
        HttpServletRequest req = request;
        AdminLoginLog sysLoginLog = new AdminLoginLog();
        sysLoginLog.setAdminNo(shiro.getAdminNo());
        sysLoginLog.setAdminName(shiro.getRealName());
        sysLoginLog.setLoginIp(IpUtils.getIpAddr(req));
        sysLoginLog.setLoginTime(new Date());
        String clientIP = IpUtils.getClientIP(request);
        sysLoginLog.setLoginIp(clientIP);
        try {
            adminLoginService.saveSysLoginLog(sysLoginLog);
        } catch (Exception e) {
            logger.error("保存后台登陆日志出错:", e);
        }

    }

}
