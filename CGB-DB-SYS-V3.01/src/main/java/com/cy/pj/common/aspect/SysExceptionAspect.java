package com.cy.pj.common.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class SysExceptionAspect {

	/**
	 * 异常通知(通常可以在这样的通知中对异常信息进行记录)
	 * @param e 异常对象
	 * 在@AfterThrowing中throwing属性的名字需要与方法参数异常类型的参数名相同
	 */
	@AfterThrowing(pointcut = "bean(sysLogServiceImpl)",throwing = "e")
	public void doHandleException(Throwable e) {
		log.error("SysExceptionAspect's exception {}", e.getMessage());
	}
}
