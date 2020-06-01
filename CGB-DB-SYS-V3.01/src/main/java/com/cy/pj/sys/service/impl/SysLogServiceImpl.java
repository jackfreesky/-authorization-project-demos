package com.cy.pj.sys.service.impl;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.cy.pj.common.exception.ServiceException;
import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.dao.SysLogDao;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@Service
public class SysLogServiceImpl implements SysLogService {

	@Autowired
	private SysLogDao sysLogDao;
	
	@Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(SysLog entity) {
		String tName=Thread.currentThread().getName();
		System.out.println(tName+"-->SysLogServiceImpl.saveObject");
		try{Thread.sleep(5000);}catch (Exception e) {}
		sysLogDao.insertObject(entity);
	}
	/**
	 * 当我们的业务方法需要授权访问时，需要通过shiro框架中的@RequiresPermissions
	 * 注解对方法进行描述。底层会基于AOP思想进行权限控制。
	 * 当我们使用@RequiresPermissions注解描述方法时，表示此方法为一个切入点方法，
	 * 执行此方法就需要进行权限检测(判定用户有没有权限执行此方法)，当用户有此方法
	 * 的访问权限时，就会授权用户方法此方法，没有权限底层要抛出异常，告诉用户你没有
	 * 此方法的操作权限。
	 * 
	 * @RequiresPermissions 注解中定义字符串表示方法此方法需要的权限。
	 */
	@RequiresPermissions("sys:log:delete")
	@Override
	public int deleteObjects(Integer... ids) {
		//1.参数校验
		if(ids==null||ids.length==0)
			throw new IllegalArgumentException("参数值无效");
		//2.基于id删除记录
		int rows=-1;
		try {
		 rows=sysLogDao.deleteObjects(ids);//后续对这样的方法调用也要进行异常监控
		}catch(Throwable e) {
			//报警，给运维人员发短信。
		  throw new ServiceException("远端数据访问暂时维护中");
		}
		//3.结果校验并返回
		if(rows==0)
			throw new ServiceException("记录可能已经不存在");
		return rows;
	}
	
	
	
	@Override
	public PageObject<SysLog> findPageObjects(String username,
			Integer pageCurrent) {
		//1.参数校验
		//说明：对于所有的检验，非空校验一定要放在第一步。
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值不正确");
		//2.查询总记录数
		int rowCount=sysLogDao.getRowCount(username);
		if(rowCount==0)
			throw new ServiceException("没有找到对应的记录");
		//3.查询当前页要呈现的记录
		int pageSize=3;//页面大小(每页最多呈现多少条记录)
		int startIndex=(pageCurrent-1)*pageSize;//计算起始位置
		List<SysLog> records=
		sysLogDao.findPageObjects(username, startIndex, pageSize);
		//4.对数据进行封装并返回
		//说明：构建对象时，参数的顺序是怎样的要结合你的构造方法的定义
		return new PageObject<>(pageCurrent, rowCount, pageSize, records);
	}

}
