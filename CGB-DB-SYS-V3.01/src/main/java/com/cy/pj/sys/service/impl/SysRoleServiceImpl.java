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
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;
import com.cy.pj.sys.vo.SysRoleMenuVo;

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
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("当前页码值不正确");
		//2.总记录数查询并校验
		int rowCount=sysRoleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		//3.当前页记录查询
		int pageSize=3;//页面大小
		int startIndex=(pageCurrent-1)*pageSize;//当前页起始位置
		List<SysRole> records=
		sysRoleDao.findPageObjects(name, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(pageCurrent, rowCount, pageSize, records);
	}

}
