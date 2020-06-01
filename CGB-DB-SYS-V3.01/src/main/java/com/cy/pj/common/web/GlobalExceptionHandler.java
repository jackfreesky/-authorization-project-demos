package com.cy.pj.common.web;

import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cy.pj.common.vo.JsonResult;
/**
 * 由@ControllerAdvice注解描述的类为控制层全局异常处理类，在此类中可以
 * 基于业务定义多个异常处理方法。
 * @author qilei
 */
//@ControllerAdvice
//@ResponseBody
@RestControllerAdvice //@ControllerAdvice+@ResponseBody
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ShiroException.class)
	public JsonResult doShiroException(ShiroException e) {
		JsonResult result=new JsonResult();
		result.setState(0);
		if(e instanceof UnknownAccountException) {
			result.setMessage("账户不存在");
		}else if (e instanceof LockedAccountException) {
			result.setMessage("账户被锁定");
		}else if(e instanceof IncorrectCredentialsException) {
			result.setMessage("密码不正确");
		}else if(e instanceof AuthorizationException) {
			result.setMessage("您没有此操作权限");
		}
		e.printStackTrace();
		return result;
	}
	/**
	 * 所有由@ExceptionHandler注解描述的方法为异常处理方法，此注解中定义的异常类型
	 * 为所描述的方法可以处理的异常类型(也可以处理其异常子类类型的异常)
	 * @param e 异常，此异常应与@ExceptionHandler注解中定义的异常保持一致
	 * @return
	 */
	//@ResponseBody
	@ExceptionHandler(RuntimeException.class)
	public JsonResult doHandleRuntimeException(RuntimeException e) {
		System.out.println("GlobalExceptionHandler.doHandleRuntimeException");
		e.printStackTrace();
		return new JsonResult(e);
	}
	
//	@ResponseBody
//	@ExceptionHandler(IllegalArgumentException.class)
//	public JsonResult doHandleIllegalArgumentException(IllegalArgumentException e) {
//		System.out.println("GlobalExceptionHandler.doHandleIllegalArgumentException");
//		e.printStackTrace();
//		return new JsonResult(e);
//	}
	
	//。。。。。。
	
}
