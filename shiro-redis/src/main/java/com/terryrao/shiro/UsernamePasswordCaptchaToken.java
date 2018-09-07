package com.terryrao.shiro;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 扩展默认的用户认证的bean(增加验证码)
 * 2015 2015年6月26日
 *
 * @author liuwenbin
 * @since 1.0
 */
public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    /**
     * UsernamePasswordCaptchaToken.java long
     */
    private static final long serialVersionUID = -8724918483366539779L;

    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaToken() {
        super();

    }

    public UsernamePasswordCaptchaToken(String username, char[] password, String host,
                                        String captcha) {
        super(username, password, host);
        this.captcha = captcha;
    }
}
