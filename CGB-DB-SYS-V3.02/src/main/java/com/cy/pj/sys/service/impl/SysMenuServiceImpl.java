package com.cy.pj.sys.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.Node;
import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysMenu;
import com.cy.pj.sys.service.SysMenuService;
import com.cy.pj.sys.vo.SysUserMenuVo;

@Service
public class SysMenuServiceImpl implements SysMenuService {

	@Autowired
	private SysMenuDao sysMenuDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	/**
	 * 基于登陆用户id查询当前用户可以访问的一级菜单和二级菜单。
	 * 方案2：
	 */
	@Override
	public List<SysUserMenuVo> findUserMenus(Integer userId) {
		//1.基于用户id获取用户对应的角色id
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(userId);
		//2.基于角色id获取角色对应的菜单id
		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
		//3.获取所有系统菜单
		List<Map<String,Object>> allMenus=sysMenuDao.findObjects();
		//4.从所有系统菜单中提取用户菜单
		//4.1构建用户菜单集合
		List<SysUserMenuVo> userMenus=new ArrayList<>();
		//4.2提取用户一级菜单
		getFirstLevelMenus(menuIds, allMenus, userMenus);
		//4.3获取用户二级菜单
		getSecondLevelMenus(menuIds, allMenus, userMenus);
		return userMenus;
	}
	
	private void getSecondLevelMenus(List<Integer> menuIds, List<Map<String, Object>> allMenus,
			List<SysUserMenuVo> userMenus) {
		for(int i=0;i<userMenus.size();i++) {//迭代用户一级菜单
			//4.3.1构建用户二级菜单集合
			List<SysUserMenuVo> childs=new ArrayList<>();
			SysUserMenuVo firstLevelMenu=userMenus.get(i);
			//4.3.2从所有系统菜单中提取用户二级菜单
			for(int j=0;j<allMenus.size();j++) {
				Map<String,Object> map=allMenus.get(j);
				if(firstLevelMenu.getId().equals(map.get("parentId"))) {
					if(menuIds.contains(map.get("id"))) {
						SysUserMenuVo um=new SysUserMenuVo();
						um.setId((Integer)map.get("id"));
						um.setName((String)map.get("name"));
						um.setUrl((String)map.get("url"));
						childs.add(um);
					}
				}
			}
			firstLevelMenu.setChilds(childs);
		}
	}
	private void getFirstLevelMenus(List<Integer> menuIds, List<Map<String, Object>> allMenus,
			List<SysUserMenuVo> userMenus) {
		for(int i=0;i<allMenus.size();i++) {
			Map<String,Object> menu=allMenus.get(i);
			Object parentId=menu.get("parentId");
			Object menuId=menu.get("id");
			if(parentId==null&&menuIds.contains(menuId)) {
				SysUserMenuVo um=new SysUserMenuVo();
				um.setId((Integer)menuId);
				um.setName((String)menu.get("name"));
				um.setUrl((String)menu.get("url"));
				userMenus.add(um);
			}
		}
	}
/**
 * 基于登陆用户id查询当前用户可以访问的一级菜单和二级菜单。
 * 方案1：
 */

//	@Override
//	public List<SysUserMenuVo> findUserMenus(Integer userId) {
//		//1.基于用户id获取用户对应的角色id
//		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(userId);
//		//2.基于角色id获取角色对应的菜单id
//		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds);
//		//3.基于菜单id获取菜单相关信息
//		List<SysUserMenuVo> list=sysMenuDao.findMenusByIds(menuIds);
//		return list;
//	}
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
