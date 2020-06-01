package com.cy.pj.common.util;

import org.apache.shiro.SecurityUtils;

import com.cy.pj.sys.entity.SysUser;

public class ShiroUtils {

	public static String getUsername() {
		return getUser().getUsername();
	}
	/**
	 * 获取登陆用户信息
	 * @return
	 */
	public static SysUser getUser() {
		return (SysUser)SecurityUtils.getSubject().getPrincipal();
	}
}
