package com.cy.pj.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 * 此DAO对象用于操作角色菜单关系表:sys_role_menus
 * @author qilei
 */
@Mapper
public interface SysRoleMenuDao {
	
	/**
	 * 基于多个角色id获取对应的菜单id
	 * @param roleId
	 * @return
	 */
	List<Integer> findMenuIdsByRoleIds(Integer[] roleIds);
	/**
	 * 基于角色id获取菜单id
	 * @param roleId
	 * @return
	 */
	List<Integer> findMenuIdsByRoleId(Integer roleId);
	/**
	 * 保存角色和菜单的关系数据
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	int insertObjects(Integer roleId,Integer[]menuIds);
    /**
     * 基于菜单id删除角色菜单关系数据
     * @param id
     * @return
     */
	@Delete("delete from sys_role_menus where menu_id=#{id}")
	int deleteObjectsByMenuId(Integer id);
	
	/**
	 * 基于角色id删除角色和菜单关系数据
	 * @param id
	 * @return
	 */
	@Delete("delete from sys_role_menus where role_id=#{id}")
	int deleteObjectsByRoleId(Integer id);
	
	
	
	
	
	
}
