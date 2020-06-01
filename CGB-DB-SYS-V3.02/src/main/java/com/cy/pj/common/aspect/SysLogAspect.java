package com.cy.pj.common.aspect;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cy.pj.common.annotation.RequiredLog;
import com.cy.pj.common.util.IPUtils;
import com.cy.pj.common.util.ShiroUtils;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
//@Order(1)
@Slf4j
@Aspect
@Component
public class SysLogAspect {
	/**
	 * @Pointcut 注解通过切入点表达式定义切入点，例如
	 * bean表达式:bean(bean的名称)
	 */
	//@Pointcut("bean(sysUserServiceImpl)")
	@Pointcut("@annotation(com.cy.pj.common.annotation.RequiredLog)")
	public void doLogPointCut() {
		//方法体不需要写任何内容
	}
    /**
     * @Around 注解描述的方法为一个通知方法，在这个方法内部可以通过连接点
     * 对象（ProceedingJoinPoint）调用目标方法，并在目标方法对象执行之前
     * 或之后添加额外功能。
     * 
     * @Around 注解描述的方法有一定要求：
     * 1)返回值类型为Object
     * 2)方法参数类型为ProceedingJoinPoint类型
     * 3)方法抛出throwable异常(建议)
     * @param jp 封装了正要执行的目标方法信息
     * @return 方法返回值为目标方法的执行结果
     * @throws Throwable
     */
	@Around("doLogPointCut()")
    public Object around(ProceedingJoinPoint jp)throws Throwable{
		long t1=System.currentTimeMillis();
		log.info("start：{}",t1);//{}在这里表示占位符
		try {
		Object result=jp.proceed();//调用本类内部切入点对应其它通知或其它切面或目标方法。
		long t2=System.currentTimeMillis();
		log.info("end: {}",t2);
		saveLog(jp,(t2-t1));//记录正常行为日志
		return result;
		}catch (Throwable e) {
		log.error("error end: {}",e.getMessage());
		//可以在此位置记录异常行为日志
		//return null;
		throw e;
		}
    }
	
	@Autowired
	private SysLogService sysLogService;
	/**
	 * 保存用户行为信息
	 * @param jp 连接点对象，此对象封装了要执行的目标方法信息
	 * @param time
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws JsonProcessingException 
	 */
	private void saveLog(ProceedingJoinPoint jp,long time) throws NoSuchMethodException, SecurityException, JsonProcessingException {
		//1.获取用户行为信息
		//1.1获取目标方法对象
		Method targetMethod=getTargetMethod(jp);
		//1.2获取目标方法的方法名信息
		String targetMethodName=
	    targetMethod.getDeclaringClass().getName()+"."+targetMethod.getName();
		//1.3获取目标方法上的操作名
		String operation = getOperation(targetMethod);
		//1.4 目标方法参数(转换为字符串)
		//String params=Arrays.toString(jp.getArgs());
		String params=
		//将参数对象转换为json格式字符串(借助jackson api)
		new ObjectMapper().writeValueAsString(jp.getArgs());
		//2.封装用户行为信息
		SysLog log=new SysLog();
		log.setIp(IPUtils.getIpAddr());
		log.setUsername(ShiroUtils.getUsername());//后续做完登陆以后，为登陆用户名
		log.setOperation(operation);
		log.setMethod(targetMethodName);
		log.setParams(params);
		log.setTime(time);
		log.setCreatedTime(new Date());
		//3.保存用户行为信息
		sysLogService.saveObject(log);
		
//		new Thread() {//频繁创建和销毁线程也会占用资源
//			public void run() {
//				sysLogService.saveObject(log);
//			};
//		}.start();
	}
	//获取目标方法
	private Method getTargetMethod(ProceedingJoinPoint jp) throws NoSuchMethodException, SecurityException {
		Class<?> targetCls=jp.getTarget().getClass();
		MethodSignature ms=(MethodSignature)jp.getSignature();
		//获取目标方法
		Method targetMethod=//目标方法(类全名+方法名)
		targetCls.getDeclaredMethod(ms.getName(),ms.getParameterTypes());
		return targetMethod;
	}
	//获取操作名
	private String getOperation(Method targetMethod) {
		String operation="operation";
		RequiredLog requiredLog=
		targetMethod.getAnnotation(RequiredLog.class);
		if(requiredLog!=null) {
		operation=requiredLog.value();
		}
		return operation;
	}
}
