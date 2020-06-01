package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.sys.entity.SysRole;
import com.cy.pj.sys.service.SysRoleService;

@RestController
@RequestMapping("/role/")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@GetMapping("doFindRoles")
	public JsonResult doFindObjects() {
		return new JsonResult(sysRoleService.findObjects());
	}
	
	@GetMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(sysRoleService.findObjectById(id));
	}
	
	@PostMapping("doUpdateObject")
	public JsonResult doUpdateObject(SysRole entity,Integer[]menuIds) {
		sysRoleService.updateObject(entity, menuIds);
		return new JsonResult("update ok");
	}
	@PostMapping("doSaveObject")
	public JsonResult doSaveObject(SysRole entity,Integer[]menuIds) {
		sysRoleService.saveObject(entity, menuIds);
		return new JsonResult("save ok");
	}
	
	//@RequestMapping("doDeleteObject")
	@DeleteMapping("doDeleteObject")//底层请求方式为delete方式
	//@ResponseBody
	public JsonResult doDeleteObject(Integer id) {
		sysRoleService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	
	
	@GetMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(Integer pageCurrent,String name) {
		return new JsonResult(sysRoleService.findPageObjects(name, pageCurrent));
	}
}
