package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.vo.SysUserDeptVo;

@Mapper
public interface SysUserDao {
	/**
	 * 基于用户id修改密码
	 * @param newPassword
	 * @param newSalt
	 * @param id
	 * @return
	 */
	@Update("update sys_users set password=#{newPassword},salt=#{newSalt},modifiedTime=now() where id=#{id}")
	int updatePassword(String newPassword,String newSalt,Integer id);
	/**
	 * 基于用户名查询用户信息
	 * @param username
	 * @return
	 */
	@Select("select * from sys_users where username=#{username}")
	SysUser findUserByUserName(String username);
	
	/**
	 * 基于用户id获取用户以及用户对应的部门信息
	 * @param id
	 * @return
	 */
	SysUserDeptVo findObjectById(Integer id);
	/**
	 * 更新用户自身信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysUser entity);
	/**
	 * 将用户自身信息写入到数据库
	 * @param entity
	 * @return
	 */
	int insertObject(SysUser entity);
	/**
	 * 禁用或启用用户状态
	 * @param id
	 * @param valid
	 * @param user
	 * @return
	 */
	@Update("update sys_users set valid=#{valid},modifiedUser=#{modifiedUser},modifiedTime=now() where id=#{id}")
	int validById(Integer id,Integer valid,String modifiedUser);

	/**
	 * 基于条件统计记录总数
	 * @param username
	 * @return
	 */
	int getRowCount(String username);
	
	/**
	 * 基于条件进行分页查询
	 * @param username
	 * @param startIndex 当前页起始位置
	 * @param pageSize 页面大小
	 * @return 当前页记录
	 */
	List<SysUserDeptVo> findPageObjects(String username,
			Integer startIndex,Integer pageSize); 
	
	
	
	
	
	
}
