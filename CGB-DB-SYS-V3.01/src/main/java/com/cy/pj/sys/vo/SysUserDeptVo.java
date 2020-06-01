package com.cy.pj.sys.vo;
import java.io.Serializable;
import java.util.Date;
import com.cy.pj.sys.entity.SysDept;
import lombok.Data;
/**
 * 何为对象序列化和反序列化以及应用场景？
 * 对象序列化id有什么作用？
 * 对象序列化是否存在安全问题？
 * 对象序列化的粒度如何进行控制？(哪些属性序列化，哪些属性不序列化)
 * Java对象序列化的性能有什么问题？
 * Java中ArrayList对象和Vector对象序列化时有什么不同？
 * 
 * 企业关注：面向对象，多线程、NIO、高级集合、Socket，HTTP协议，JVM,数据结构和算法
 */

@Data
public class SysUserDeptVo implements Serializable{
	private static final long serialVersionUID = 1138534420870847335L;
	private Integer id;
	private String username;
	private String password;//md5
	private String salt;
	private String email;
	private String mobile;
	private Integer valid=1;
	private SysDept sysDept; //private Integer deptId;
	private Date createdTime;
	private Date modifiedTime;
	private String createdUser;
	private String modifiedUser;
}
