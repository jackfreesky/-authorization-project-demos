package com.cy.pj.sys.entity;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
/**
 * 在java中所有用于存储数据的对象都可以简单理解为pojo对象，
 * 并且建议所有的这样的类型都实现Serializable接口(在java规范中只有实现了
 * 此接口的对象才可以支持序列化和反序列化操作)。
 */

@Data
public class SysLog implements Serializable{
	//序列化id，实现了序列化接口的类都要生成这样的一个序列化id
	private static final long serialVersionUID = 8924387722922123121L;
	private Integer id;
	//用户名
	private String username;
	//用户操作
	private String operation;
	//请求方法
	private String method;
	//请求参数
	private String params;
	//执行时长(毫秒)
	private Long time;
	//IP地址
	private String ip;
	//创建时间
	private Date createdTime;
	
	

}
