package com.cy.pj.sys.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cy.pj.sys.entity.SysLog;
/**
 * 日志模块的数据层对象：负责通过mybatis实现与数据库的会话
 * @author qilei
 */
@Mapper
public interface SysLogDao {
	
	 
	 int insertObject(SysLog entity);
	 /**
	  * 基于多个记录id执行日志信息删除
	  * @param ids 存储了多个id值的可变参数(特殊数组)
	  * @return
	  */
	  //假如没有使用@Params注解描述参数，在SQL映射中使用array去接收参数值
	  int deleteObjects(@Param("ids")Integer...ids);
	  
	  /**
	   * 基于条件查询当前页要呈现的记录
	   * @param username 用户名
	   * @param startIndex 当前页的起始位置
	   * @param pageSize 页面大小
	   * @return 当前页的记录
	   */
	  List<SysLog> findPageObjects(String username);
	
}  





