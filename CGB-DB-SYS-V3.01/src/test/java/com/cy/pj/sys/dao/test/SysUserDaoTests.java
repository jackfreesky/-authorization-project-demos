package com.cy.pj.sys.dao.test;

import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.dao.SysUserDao;
import com.cy.pj.sys.vo.SysUserDeptVo;

@SpringBootTest
public class SysUserDaoTests {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Test
	void testGetRowCount() {
		int rowCount=
		sysUserDao.getRowCount("admin");
		System.out.println("rowCount="+rowCount);
	}
	
	@Test
	public void testFindPageObjects() {
		List<SysUserDeptVo> list=
		sysUserDao.findPageObjects(null, 0, 5);
		for(SysUserDeptVo user:list) {
			System.out.println(user);
		}
		
//		list.forEach(new Consumer<SysUserDeptVo>() {
//			@Override
//			public void accept(SysUserDeptVo t) {
//				System.out.println(t);
//			}
//		});
		//jdk8 lamda表达式
		//list.forEach((t)->System.out.println(t));
		//jdk8 中的新方法引用
		//list.forEach(System.out::println);
		
	}
	
	
}
