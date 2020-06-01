package com.cy.pj.sys.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
/**
 * 封装用户权限菜单信息
 * 1)基于用户id获取用户对应的权限菜单信息。
 * a)基于用户id查找用户对应的角色id
 * b)基于角色id查找角色对应的菜单id
 * c)基于菜单id获取对应的一级菜单和二级菜单。
 * 2)将菜单信息封装到此VO对象。
 * @author qilei
 */
@Data
public class SysUserMenuVo implements Serializable{
	 private static final long serialVersionUID = -7234863379941761458L;
	 /** 菜单id */
	 private Integer id;
	 /** 菜单名*/
	 private String name;
	 /** 菜单url */
	 private String url;
	 /**菜单对应的子菜单*/
	 private List<SysUserMenuVo> childs;
}
