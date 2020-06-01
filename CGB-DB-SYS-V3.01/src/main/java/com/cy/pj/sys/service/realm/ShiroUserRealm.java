package com.cy.pj.sys.service.realm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cy.pj.sys.dao.SysMenuDao;
import com.cy.pj.sys.dao.SysRoleMenuDao;
import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.dao.SysUserRoleDao;
import com.cy.pj.sys.entity.SysUser;
@Service //<bean id="" class="">
public class ShiroUserRealm extends AuthorizingRealm {
	
	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	private SysUserRoleDao sysUserRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	
	@Autowired
	private SysMenuDao sysMenuDao;
    
	/**
	 * 获取认证用户的权限信息并进行封装
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		System.out.println("==doGetAuthorizationInfo==");
		//1.获取登陆用户id
		SysUser user=(SysUser)principals.getPrimaryPrincipal();
		Integer userId=user.getId();
		//2.基于登陆用户id获取用户角色id (sys_user_roles)
		List<Integer> roleIds=sysUserRoleDao.findRoleIdsByUserId(userId);
		if(roleIds==null||roleIds.size()==0)
			throw new AuthorizationException();
		//3.基于用户角色id获取角色对应的菜单id (sys_role_menus)
		List<Integer> menuIds=sysRoleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(new Integer[] {}));
		if(menuIds==null||menuIds.size()==0)
			throw new AuthorizationException();
		//4.基于菜单id获取用户对应的权限标识permission (sys_menus)
		List<String> permissions=
		sysMenuDao.findPermissions(menuIds.toArray(new Integer[] {}));
		if(permissions==null||permissions.size()==0)
			throw new AuthorizationException();
		//5.封装用户权限信息
		SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
		Set<String> stringPermissions=new HashSet<>();
//		for (int i = 0; i < permissions.size(); i++) {
//			if(!StringUtils.isEmpty(permissions.get(i))) {
//				stringPermissions.add(permissions.get(i));
//			}
//		}
		for(String per:permissions) {
			if(!StringUtils.isEmpty(per)) {
				stringPermissions.add(per);
			}
		}
		System.out.println("user.permisssions="+stringPermissions);
		info.setStringPermissions(stringPermissions);
		return info;//返回给securityManager
	}

	/**
	 * 实现认证信息的获取以及封装
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//1.获取登陆用户名
		UsernamePasswordToken upToken=(UsernamePasswordToken)token;
		String username=upToken.getUsername();
		//2.基于用户名查询用户信息
		SysUser user=sysUserDao.findUserByUserName(username);
		//3.判定用户是否存在
		if(user==null)throw new UnknownAccountException();
		//4.判定用户是否被禁用
		if(user.getValid()==0)throw new LockedAccountException();
		//5.对用户凭证信息进行封装并返回
		ByteSource credentialsSalt=ByteSource.Util.bytes(user.getSalt());
		SimpleAuthenticationInfo info=
		new SimpleAuthenticationInfo(
				user,//principal 用户身份
				user.getPassword(), //hashedCredentials 已加密的密码
				credentialsSalt, //credentialsSalt 凭证盐
				getName());//realmName
		return info;//此对象会返回给认证管理器
	}
	/**
	 * 设置凭证匹配器
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
		cMatcher.setHashAlgorithmName("MD5");
		cMatcher.setHashIterations(1);
		super.setCredentialsMatcher(cMatcher);
	}
	
//	@Override
//	public CredentialsMatcher getCredentialsMatcher() {
//		HashedCredentialsMatcher cMatcher=new HashedCredentialsMatcher();
//		cMatcher.setHashAlgorithmName("MD5");
//		cMatcher.setHashIterations(1);
//		return cMatcher;
//	}

}
