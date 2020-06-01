package com.cy.pj.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cy.pj.common.vo.JsonResult;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

//@Controller
//@ResponseBody
@RestController //==@Controller+@ReponseBody
@RequestMapping("/log/")
public class SysLogController {//BaseController
    
	@Autowired
	private SysLogService sysLogService;

	@RequestMapping("doDeleteObjects")
	//@ResponseBody
	public JsonResult doDeleteObjects(Integer...ids) {
		sysLogService.deleteObjects(ids);
		return new JsonResult("delete ok");
	}	
	
	@GetMapping("doFindPageObjects")
	//@ResponseBody
	public JsonResult doFindPageObjects(String username,Integer pageCurrent) {
		System.out.println("pageCurrent="+pageCurrent);
		PageObject<SysLog> pageObject=
		sysLogService.findPageObjects(username, pageCurrent);
		//对业务层返回的数据进行封装，为其添加状态信息，便于客户端进行处理
		//JsonResult result=new JsonResult();
		//result.setData(pageObject);
		//return result;
		return new JsonResult(pageObject);
	}
	/**
	 * 在控制层方法内部定义异常处理方法
	 * @param e
	 * @return
	 */
//	@ResponseBody
//	@ExceptionHandler(RuntimeException.class)
//	public JsonResult doHandleRuntimeException(RuntimeException e) {
//		System.out.println("SysLogController.doHandleRuntimeException");
//		e.printStackTrace();
//		return new JsonResult(e);
//	}
	
}
