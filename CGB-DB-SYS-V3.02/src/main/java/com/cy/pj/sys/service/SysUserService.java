package com.cy.pj.sys.service;

import java.util.Map;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

public interface SysUserService {
	/**
	 * 修改密码
	 * @param sourcePassword
	 * @param newPassword
	 * @param configPassword
	 * @return
	 */
	int updatePassword(String sourcePassword,String newPassword,String configPassword);
	/**
	 * 基于用户id获取用户信息，用户对应的部门信息，用户对应的角色信息
	 * @param id
	 * @return
	 */
	Map<String,Object> findObjectById(Integer id);
	
	/**
	 * 更新用户以及用户对应的角色id
	 * @param entity
	 * @param roleIds
	 * @return
	 */
	 int updateObject(SysUser entity,Integer[]roleIds);
	 /**
	  * 保存用户以及用户对应的角色id
	  * @param entity
	  * @param roleIds
	  * @return
	  */
	 int saveObject(SysUser entity,Integer[]roleIds);
	/**
	 *修改用户状态 
	 */
	 int validById(Integer id,Integer valid);
     /**
      * 用户信息的分页查询
      * @param username
      * @param pageCurrent
      * @return
      */
	 PageObject<SysUserDeptVo> findPageObjects(String username,Integer pageCurrent);
}
