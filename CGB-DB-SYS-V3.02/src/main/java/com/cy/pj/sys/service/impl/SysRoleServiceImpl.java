package com.cy.pj.sys.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.AssertUtil;
import com.cy.pj.common.vo.CheckBox;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysRoleDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public List<CheckBox> findObjects() {
		// TODO Auto-generated method stub
		return sysRoleDao.findObjects();
	}
	
	
	@Override
	public SysRoleMenuVo findObjectById(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("id值无效");
		//2.查询角色自身信息
		SysRoleMenuVo rm=sysRoleDao.findObjectById(id);//单表查询
		if(rm==null)
			throw new ServiceException("记录可能已经不存在");
		//3.查询角色对应的菜单信息
		//List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleId(id);//单表查询
		//rm.setMenuIds(menuIds);
		//4.返回查询结果
		return rm;
	}
	
	@Override
	public int updateObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
	    if(StringUtils.isEmpty(entity.getName()))
	    	throw new IllegalArgumentException("角色名不能为空");
	    if(menuIds==null||menuIds.length==0)
	    	throw new IllegalArgumentException("必须为角色分配权限");
		//2.保存角色自身信息
	   int rows=sysRoleDao.updateObject(entity);
		//3.更新角色和菜单的关系数据
	   //3.1删除原有关系数据
	   sysRoleMenuDao.deleteObjectsByRoleId(entity.getId());
	   //3.2添加新的关系数据
	   sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回结果
		return rows;
	}
	@Override
	public int saveObject(SysRole entity, Integer[] menuIds) {
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getName()))
			throw new IllegalArgumentException("角色名不能为空");
		if(menuIds==null||menuIds.length==0)
			throw new IllegalArgumentException("必须为角色分配权限");
		//2.保存角色自身信息
		int rows=sysRoleDao.insertObject(entity);
		//3.保存角色和菜单的关系数据
		sysRoleMenuDao.insertObjects(entity.getId(), menuIds);
		//4.返回结果
		return rows;
	}
	
	@Override
	public int deleteObject(Integer id) {
		//1.参数校验
		//AssertUtil.isArgsValid(id==null||id<1, "id值不正确");
		if(id==null||id<1)
			throw new IllegalArgumentException("id值不正确");
		//2.删除关系数据
		//2.1删除角色菜单关系数据
		sysRoleMenuDao.deleteObjectsByRoleId(id);
		//2.2删除用户角色关系数据
		sysUserRoleDao.deleteObjectsByRoleId(id);
		//3.删除自身信息
		int rows=sysRoleDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		//4.返回结果
		return rows;
	}
	
	@Override
	public PageObject<SysRole> findPageObjects(String name, Integer pageCurrent) {
		//1.参数校验
		//说明：对于所有的检验，非空校验一定要放在第一步。
		if(pageCurrent==null||pageCurrent<1)throw new IllegalArgumentException("页码值不正确");
		Page<SysRole> page=PageHelper.startPage(pageCurrent, 3);//3为pageSize
		List<SysRole> records=sysRoleDao.findPageObjects(name);
		//4.对数据进行封装并返回
		//说明：构建对象时，参数的顺序是怎样的要结合你的构造方法的定义
		return new PageObject<>(pageCurrent, page.getPages(),page.getTotal(), page.getPageSize(), records);
	}

}
