package com.cy.pj.sys.service.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.common.vo.PageObject;
import com.cy.pj.sys.entity.SysLog;
import com.cy.pj.sys.service.SysLogService;

@SpringBootTest
public class SysLogServiceTests {

	@Autowired
	private SysLogService sysLogService;
	
	@Test
	void testFindPageObjects() {
	    PageObject<SysLog> pageObject=
		sysLogService.findPageObjects("admin", 1);
	    System.out.println("rowCount="+pageObject.getRowCount());
	    System.out.println("pageCount="+pageObject.getPageCount());
	    for(SysLog log:pageObject.getRecords()) {
	    	System.out.println(log);
	    }
	}
}
