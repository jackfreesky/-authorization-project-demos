package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import lombok.Setter;
/**
 * 通过此dao操作用户和角色关系表数据
 * @author qilei
 */
@Mapper
public interface SysUserRoleDao {
	/**
	 * 基于用户id获取用户对应的角色id
	 * @param id
	 * @return
	 */
	@Select("select role_id from sys_user_roles where user_id=#{id}")
	List<Integer> findRoleIdsByUserId(Integer id);
	/**
	 * 保存用户和角色的关系数据
	 * @param userId
	 * @param roleIds
	 * @return
	 */
	int insertObjects(Integer userId,Integer[]roleIds);

	/**
	 * 基于角色id删除用户和角色的关系数据
	 * @param id
	 * @return
	 */
	 @Delete("delete from sys_user_roles where role_id=#{id}")
	 int deleteObjectsByRoleId(Integer id);
	 
	 @Delete("delete from sys_user_roles where user_id=#{id}")
	 int deleteObjectByUserId(Integer id);
	 
	 
	 
	
}
