package com.cy.pj.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysUser;
import com.cy.pj.sys.service.SysUserService;

@RestController
@RequestMapping("/user/")
public class SysUserController {

	   @Autowired
	   private SysUserService sysUserService;
	   
	   @RequestMapping("doUpdatePassword")
	   public JsonResult doUpdatePassword(String sourcePassword,String newPassword,String configPassword) {
		   sysUserService.updatePassword(sourcePassword, newPassword, configPassword);
		   return new JsonResult("update ok");
		   
	   }
	   
	   @RequestMapping("doLogin")
	   public JsonResult doLogin(String username,String password,boolean isRememberMe) {
		   //1.获取subject对象
		   Subject subject=SecurityUtils.getSubject();
		   //2.提交用户信息进行登陆操作
		   UsernamePasswordToken token=
		   new UsernamePasswordToken(username, password);
		   //设置记住我
		   token.setRememberMe(isRememberMe);//isRememberMe假如为true，底层就会以cookie形式将用户信息写客户端
		   subject.login(token);//将token提交给securityManager对象
		   return new JsonResult("login ok");
	   }
	   
	   
	   @GetMapping("doFindObjectById")
	   public JsonResult doFindObjectById(Integer id) {
		   return new JsonResult(sysUserService.findObjectById(id));
	   }
	   
	   @PostMapping("doUpdateObject")
	   public JsonResult doUpdateObject(SysUser entity,Integer[]roleIds) {
		   sysUserService.updateObject(entity, roleIds);
		   return new JsonResult("update ok");
	   }
	   
	   @PostMapping("doSaveObject")
	   public JsonResult doSaveObject(SysUser entity,Integer[]roleIds) {
		   sysUserService.saveObject(entity, roleIds);
		   return new JsonResult("save ok");
	   }
	   
	   @RequestMapping("doValidById")
	   public JsonResult doValidById(Integer id, Integer valid){
		   sysUserService.validById(id, valid);
		   return new JsonResult("update ok");
	   }
	   
	   @GetMapping("doFindPageObjects")
	   public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		   return new JsonResult(sysUserService.findPageObjects(username, pageCurrent));
	   }
}//SysUserController-->SysUserService-->SysUserServiceImpl$$EnHancerBySpringCGLIB-->SysUserServiceImpl
