package com.cy.pj.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;
import com.cy.pj.sys.vo.SysUserDeptVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(isolation = Isolation.READ_COMMITTED,
               rollbackFor = Throwable.class,
               readOnly = false,
               timeout = 30,
               propagation = Propagation.REQUIRED)

public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Override
	public int updatePassword(String sourcePassword, String newPassword, String configPassword) {
		//1.参数校验
		//1.1非空校验
		if(StringUtils.isEmpty(sourcePassword))
			throw new IllegalArgumentException("原密码不能为空");
		if(StringUtils.isEmpty(newPassword))
			throw new IllegalArgumentException("新密码不能为空");
		//1.2两次输入的新密码是否一致
		if(!newPassword.equals(configPassword))
			throw new IllegalArgumentException("两次新密码输入不一致");
		//1.3原密码是否正确
		//1.3.1 对用户输入的原密码进行加密
		SysUser user=ShiroUtils.getUser();
		String salt=user.getSalt();
		SimpleHash sh=new SimpleHash("MD5", sourcePassword, salt, 1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new IllegalArgumentException("原密码不正确");
		//2.修改密码
		//2.1获取新的盐值
		String newSalt=UUID.randomUUID().toString();
		//2.2对新密码进行加密
		sh=new SimpleHash("MD5", newPassword, newSalt, 1);
		//2.3更新新密码
		int rows=sysUserDao.updatePassword(sh.toHex(), newSalt, user.getId());
		//3.返回结果
		return rows;
	}
	
	@Transactional(readOnly = true)
	@Override
	public Map<String, Object> findObjectById(Integer id) {
		//1.参数校验
		if(id==null||id<1)
			throw new IllegalArgumentException("参数不正确");
		//2.获取用户以及用户对应的部门信息
		SysUserDeptVo user=sysUserDao.findObjectById(id);
		if(user==null)
			throw new ServiceException("用户可能已经不存在了");
		//3.获取用户对应的角色信息
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(id);
		//4.封装查询结果并返回
		Map<String,Object> map=new HashMap<>();
		map.put("user", user);
		map.put("roleIds", roleIds);
		return map;
	}
	@RequiredLog("添加用户")
	@Override
	public int saveObject(SysUser entity, Integer[] roleIds) {
	    //1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
	    if(StringUtils.isEmpty(entity.getUsername()))
	    	throw new IllegalArgumentException("用户名不能为空");
	    if(StringUtils.isEmpty(entity.getPassword()))
	    	throw new IllegalArgumentException("密码不能为空");
	    if(roleIds==null||roleIds.length==0)
	    	throw new IllegalArgumentException("必须为用户分配角色");
		//2.密码加密
	    //2.1借助一个随机的字符串作为盐值(加密盐)
	    String salt=UUID.randomUUID().toString();//产生一个随机的字符串
	    //2.2对内容进行md5加密(MD5是一种消息摘要算法，特点：相同内容加密结果相同并且不可逆)
	    //2.2.1借助spring框架中的加密工具类(DigestUtils)进行加密
	    //String newPassword=DigestUtils.md5DigestAsHex((entity.getPassword()+salt).getBytes());
	    //2.2.2借助Shiro框架中的API进行密码加密
	    SimpleHash simpleHash=new SimpleHash(
	    		"MD5",//algorithmName 为算法名称
	    		entity.getPassword(), //用户端输入的密码
	    		salt,//加密盐
	    		1);//数字表示加密次数
	    entity.setSalt(salt);
	    entity.setPassword(simpleHash.toHex());
		//3.保存用户自身信息
	    int rows=sysUserDao.insertObject(entity);
		//4.保存用户和角色关系数据
	    sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		return rows;
	}
	@Override
	public int updateObject(SysUser entity, Integer[] roleIds) {
		//开启事务
		//1.参数校验
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(entity.getUsername()))
			throw new IllegalArgumentException("用户名不能为空");
		if(roleIds==null||roleIds.length==0)
			throw new IllegalArgumentException("必须为用户分配角色");
		//3.更新用户自身信息
		int rows=sysUserDao.updateObject(entity);
		//4.更新用户和角色关系数据
		sysUserRoleDao.deleteObjectByUserId(entity.getId());
		sysUserRoleDao.insertObjects(entity.getId(), roleIds);
		//提交事务
		//回滚事务
		return rows;
	}
	@RequiresPermissions("sys:user:update")
	@RequiredLog("禁用启用")
	@Override
	public int validById(Integer id, Integer valid) {
		//1.校验参数
		if(id==null||id<1) {
			log.error("id value is {}", id);
			throw new IllegalArgumentException("id值无效");
		}
		if(valid!=0&&valid!=1){
			log.error("valid value is {}", valid);
			throw new IllegalArgumentException(valid+" 状态值无效");
		}
		//2.更新用户状态
		int rows=sysUserDao.validById(id, valid, "admin");//将来此用户为登陆
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	
	//@Cacheable(value = "userCache")
	@Transactional(readOnly = true)
	@RequiredLog("查询用户")
	@Override
	public PageObject<SysUserDeptVo> findPageObjects(String username, Integer pageCurrent) {
		
		String tName=Thread.currentThread().getName();
		System.out.println(tName+"-->SysUserServiceImpl.findPageObjects");
		//1.参数校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值不正确");
		//2.查询总记录数
		int rowCount=sysUserDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		//3.查询当前页要呈现的记录
		int pageSize=5;
		int startIndex=(pageCurrent-1)*pageSize;
		List<SysUserDeptVo> records=
		sysUserDao.findPageObjects(username, startIndex, pageSize);
		//4.封装查询结果并返回
		return new PageObject<>(pageCurrent, rowCount, pageSize, records);
	}

}
