package com.cy.pj.sys.service;

import java.util.List;
import java.util.Map;

import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.entity.SysMenu;

public interface SysMenuService {
	/**
	 * 更新菜单信息
	 * @param entity
	 * @return
	 */
	int updateObject(SysMenu entity);
	/**
	 * 保存菜单信息
	 * @param entity
	 * @return
	 */
	int saveObject(SysMenu entity);
	
	List<Node> findZtreeMenuNodes();
	
	  /**
	   * 基于菜单id删除角色菜单关系数据以及菜单自身信息
	   * @param id
	   * @return
	   */
	  int deleteObject(Integer id);

	  List<Map<String,Object>> findObjects();
}
