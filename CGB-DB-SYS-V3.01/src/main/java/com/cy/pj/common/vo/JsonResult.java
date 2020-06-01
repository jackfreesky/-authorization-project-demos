package com.cy.pj.common.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JsonResult implements Serializable{//Result,R
	private static final long serialVersionUID = 677048178703375661L;
	/**状态码:用于标识服务端要返回给客户端的数据是正常还是异常数据*/
	private int state=1;//1默认标识ok，0标识error
	/**状态码对应的消息*/
	private String message="ok";
	/**通过此属性存储业务层的正常数据，尤其是查询*/
	private Object data;
	
	//public JsonResult(){}//@NoArgsConstructor用于告诉lombok生成无参构造函数
	
	public JsonResult(String message) {
		this.message=message;
	}
	public JsonResult(Object data) {
		this.data=data;
	}
	public JsonResult(Throwable e) {//Throwable是所有Error,Exception的父类
		this.state=0;//error
		this.message=e.getMessage();//异常信息
	}
	//.....
}






