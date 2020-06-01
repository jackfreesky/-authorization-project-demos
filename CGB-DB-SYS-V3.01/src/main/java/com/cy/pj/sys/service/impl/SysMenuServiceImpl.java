package com.cy.pj.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int updateObject(SysMenu entity) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(entity.getName()==null||"".equals(entity.getName().trim()))
			throw new IllegalArgumentException("菜单名不允许为空");
		//...
		//2.执行更新操作
		int rows=sysMenuDao.updateObject(entity);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//3.返回结果
		return rows;
	}
	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int saveObject(SysMenu entity) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(entity.getName()==null||"".equals(entity.getName().trim()))
			throw new IllegalArgumentException("菜单名不允许为空");
		//...
		//2.执行保存操作
		int rows=sysMenuDao.insertObject(entity);
		//3.返回结果
		return rows;
	}
	
	@Override
	public List<Node> findZtreeMenuNodes() {
		// TODO Auto-generated method stub
		return sysMenuDao.findZtreeMenuNodes();
	}
	//@CacheEvict表示要从cache中移除数据，allEntries表示移除所有
	@CacheEvict(value = "menuCache",allEntries = true)
	@Override
	public int deleteObject(Integer id) {
		//1.参数有效性校验
		if(id==null||id<1)
			throw new IllegalArgumentException("参数不合法");
		//2.检查当前菜单有没有子菜单
		int rowCount=sysMenuDao.getChildCount(id);
		if(rowCount>0)
			throw new ServiceException("有子菜单不允许删除");
		//3.删除角色菜单关系数据
		sysRoleMenuDao.deleteObjectsByMenuId(id);
		//4.删除菜单自身信息
		int rows=sysMenuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//5.返回结果
		return rows;
	}
	//@Cacheable 描述方法时，表示要将方法的返回值存储到cache对象
	@Cacheable(value = "menuCache")//value属性的值为cache对象的名字
	@Override
	public List<Map<String, Object>> findObjects() {
		// TODO Auto-generated method stub
		return sysMenuDao.findObjects();
	}

}
