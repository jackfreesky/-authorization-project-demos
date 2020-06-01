package com.cy.pj.common.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.cy.pj.common.exception.ServiceException;
/**
 * 自定义spring mvc 拦截器，通过此拦截实现登陆操作的限定时间访问
 * @author qilei
 */
public class TimeAccessInterceptor implements HandlerInterceptor {
	/**
	 * 在后端handler(Controller)方法执行之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("===preHandle===");
		Calendar c=Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,6);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		long start=c.getTimeInMillis();
		c.set(Calendar.HOUR_OF_DAY, 20);
		long end=c.getTimeInMillis();
		long current=System.currentTimeMillis();
		if(current<start||current>end)
			throw new ServiceException("不在访问时间之内");
		return true;//false拒绝继续执行，true表示放行
	}

}
