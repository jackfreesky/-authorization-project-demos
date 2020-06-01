package com.cy.pj.sys.vo;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class SysRoleMenuVo implements Serializable{
	private static final long serialVersionUID = -7213694248989299601L;
	private Integer id;
	private String name;
	private String note;
	/**当前角色对应的菜单id*/
	private List<Integer> menuIds;
}
