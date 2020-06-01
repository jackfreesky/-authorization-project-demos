package com.cy.pj.common.vo;
import java.io.Serializable;

import lombok.Data;
/**
 * 借助此对象封装树节点信息，像这样的类，该定义哪些属性由业务而定。
 */
@Data
public class Node implements Serializable{
	private static final long serialVersionUID = 2048083156365694892L;
	private Integer id;
	private String name;
	private Integer parentId;
}
