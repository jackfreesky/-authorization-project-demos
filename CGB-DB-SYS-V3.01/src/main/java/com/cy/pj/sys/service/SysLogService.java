package com.cy.pj.sys.service;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;

/**
 * 基于此接口规范完成日志模块的业务处理操作,例如
 * 1)核心业务(参数校验，核心数据处理，结果校验，异常处理，数据的封装及返回)
 * 2)拓展业务(例如权限控制，异步处理，事务处理，...)
 * @author qilei
 */
public interface SysLogService {
	
	 void saveObject(SysLog entity);
	
	  /**
	   * 基于多个id删除日志记录
	   * @param ids
	   * @return 删除的行数
	   */
	  int deleteObjects(Integer...ids);
	
      /**
       * 基于条件分查询日志记录信息
       * @param username 查询条件，基于用户名进行模糊查询
       * @param pageCurrent 当前页码值
       * @return 查询到的结果
       */
	  PageObject<SysLog> findPageObjects(String username,Integer pageCurrent);
	
}
