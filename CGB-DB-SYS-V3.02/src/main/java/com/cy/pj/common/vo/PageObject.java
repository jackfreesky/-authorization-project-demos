package com.cy.pj.common.vo;
import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * VO(ValueObject):值对象(特殊的pojo对象)
 * 用于在业务层封装分页信息的一个对象
 * @author qilei
 */
@Data
@AllArgsConstructor
public class PageObject<T> implements Serializable {
	private static final long serialVersionUID = 1684876792546164773L;
	/**当前页码值*/
	private Integer pageCurrent;
	/**总页数*/
	private Integer pageCount;
	/**总记录数*/
	private Long rowCount;
	/**记录页面大小*/
	private Integer pageSize;
	/**记录从数据库查询到的当前页记录*/
	private List<T> records;
	public PageObject() {}
}
