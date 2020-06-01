package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.entity.SysRole;

@SpringBootTest
public class MyBatisCacheTests {

	   @Autowired
	   private SqlSessionFactory sqlSessionFactory;
	   /**
	    * 测试mybatis中的一级缓存：此缓存不可以跨session实现数据共享
	    */
	   @Test
	   void testFirstLevelCache() {
		   //1.创建SQLSession对象
		   SqlSession session1=sqlSessionFactory.openSession();
		   //2.执行会话查询操作
		   SysMenuDao sysMenuDao=session1.getMapper(SysMenuDao.class);
		   List<Map<String,Object>> list1=sysMenuDao.findObjects();
		   //session1.commit(); 事务一旦提交，一级缓存中的数据会清除
		   //同一个sqlSession对象的第二次同样的查询，数据会从缓存获取。
		   List<Map<String,Object>> list2=sysMenuDao.findObjects();
		   System.out.println(list1==list2);//true
		   //3.释放资源
		   session1.close();
	   }
	   /**
	    * 此时mybatis二级缓存，此缓存可以跨session(也就是说多个会话)共享数据
	    */
	   @Test
	   void testSecondLevelCache01() {
		   //1.创建SQLSession对象
		   SqlSession session1=sqlSessionFactory.openSession();
		   SqlSession session2=sqlSessionFactory.openSession();
		   //2.执行会话查询操作
		   SysMenuDao sysMenuDao=session1.getMapper(SysMenuDao.class);
		   SysMenuDao sysMenuDao2=session2.getMapper(SysMenuDao.class);
		   List<Map<String,Object>> list1=sysMenuDao.findObjects();
		   session1.commit(); //事务一旦提交，一级缓存中的数据会清除,假如有二级缓存，数据会存储到二级缓存
		   //同一个sqlSession对象的第二次同样的查询，数据会从缓存获取。
		   List<Map<String,Object>> list2=sysMenuDao2.findObjects();
		   System.out.println(list1==list2);//true(表示list1和list2指向的是内存中的同一个对象),前提是二级缓存readonly=true
		   //3.释放资源
		   session1.close();
		   session2.close();
	   }
	   @Test
	   void testSecondLevelCache02() {
		   //1.创建SQLSession对象
		   SqlSession session1=sqlSessionFactory.openSession();
		   SqlSession session2=sqlSessionFactory.openSession();
		   //2.执行会话查询操作
		   SysRoleDao sysRoleDao01=session1.getMapper(SysRoleDao.class);
		   SysRoleDao sysRoleDao02=session2.getMapper(SysRoleDao.class);
		   List<SysRole> list1=sysRoleDao01.findPageObjects("管理员");
		   session1.commit(); //事务一旦提交，一级缓存中的数据会清除,假如有二级缓存，数据会存储到二级缓存
		   //同一个sqlSession对象的第二次同样的查询，数据会从缓存获取。
		   List<SysRole> list2=sysRoleDao02.findPageObjects("管理员");
		   System.out.println(list1==list2);//true(表示list1和list2指向的是内存中的同一个对象),前提是二级缓存readonly=true
		   //3.释放资源
		   session1.close();
		   session2.close();
	   }
}








