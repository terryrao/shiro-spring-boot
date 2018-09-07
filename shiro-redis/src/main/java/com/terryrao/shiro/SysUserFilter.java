package com.terryrao.shiro;

import com.terryrao.shiro.constant.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 将当前用户放入
 * 2015 2015年7月2日
 *
 * @author Administrator
 * @since 1.0
 */
public class SysUserFilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        request.setAttribute(Constants.CURRENT_USER, shiroUser);
        return true;
    }
}
