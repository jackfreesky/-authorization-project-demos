package com.cy.pj.common.exception;
/**
 * 自定义异常
 * 说明：一般自定义异常需要具备一定业务含义，在java中自定义异常通常要
 * 继承RuntimeException(非检查异常-编译器不对此类异常进行检查).
 * @author qilei
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 5843835376260549700L;

	public ServiceException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	
}
