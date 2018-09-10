package com.terryrao.shiro;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

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
		// （包含字母和数字）
		return Optional.ofNullable(password).filter(StringUtils::isNotBlank)
				.map(p -> !p.matches("^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$.%^&*]+$)[a-zA-Z\\d!@#$%^&*]{6,}$"))
				.orElse(true);
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
