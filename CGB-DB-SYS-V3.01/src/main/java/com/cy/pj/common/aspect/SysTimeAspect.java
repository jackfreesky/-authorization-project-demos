package com.cy.pj.common.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SysTimeAspect {

	@Pointcut("bean(sysUserServiceImpl)")
	public void doTime() {}
	/**
	 * 目标方法执行之前执行
	 */
	@Before("doTime()")
	public void doBefore() {
		System.out.println("@Before");
	}
	/**
	 * 目标方法执行之后执行，不管是否出现异常，都会执行
	 */
	@After("doTime()")
	public void doAfter() {
		System.out.println("@After");
	}
	/**
	 * 在After之后执行，前提是程序正常结束
	 */
	@AfterReturning("doTime()")
	public void doAfterReturning() {
		System.out.println("@AfterReturning");
	}
	/**
	 * After之后执行，前提是程序异常结束
	 */
	@AfterThrowing("doTime()")
	public void doAfterThrowing() {
		System.out.println("@AfterThrowing");
	}
	@Around("doTime()")
	public Object doAround(ProceedingJoinPoint jp)throws Throwable{
		System.out.println("@Around.before");
		try {
		Object result=jp.proceed();
		return result;
		}catch(Throwable e) {
		System.out.println(e.getMessage());
		throw e;
		}finally{
		System.out.println("@Around.after");
		}

	}
	
	
	
}
