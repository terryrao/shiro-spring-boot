package com.terryrao.shiro;

import org.apache.shiro.authc.AuthenticationException;

/**
 *  验证码错误异常
 * 2015 2015年6月26日
 * @author liuwenbin
 * @since 1.0
 */
public class CaptchaException extends AuthenticationException {

    /**
     * CaptchaException.java long
     */
    private static final long serialVersionUID = 4682736701364155061L;

    public CaptchaException() {

        super();

    }

    public CaptchaException(String message, Throwable cause) {

        super(message, cause);

    }

    public CaptchaException(String message) {

        super(message);

    }

    public CaptchaException(Throwable cause) {

        super(cause);

    }
}
