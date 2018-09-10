package com.terryrao.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;

public class PasswordHelper {

	// private static String algorithmName = "md5";
	// private static int hashIterations = 2;

	/**
	 * 对字符串进行加密
	 * 
	 */
	public static String encryptPassword(String password) {
		if (password == null) {
			password = "";
		}
		return UnixCrypt.crypt(password, DigestUtils.sha256Hex(password));
	}

	/**
	 * 判断密码是否为简单密码
	 * 
	 */
	public static boolean checkPasswordIsSimple(String password) {
		if (StringUtils.isBlank(password)) {
			return true;
		}
		return !password.matches("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$.%^&*]+$)[a-zA-Z\\d!@#$%^&*]{6,}$");// （包含字母和数字）
	}

	/**
	 * 判断密码是否为简单密码
	 * 
	 */
	public static boolean checkPasswordIsSimple(String password, String username) {
		// 密码为初始密码密码（密码=用户名）
		return username.equals(password) || checkPasswordIsSimple(password);
	}
	
}
