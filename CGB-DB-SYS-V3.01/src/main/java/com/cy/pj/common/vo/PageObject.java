package com.cy.pj.common.vo;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * VO(ValueObject):值对象(特殊的pojo对象)
 * 用于在业务层封装分页信息的一个对象
 * @author qilei
 */
@Data
public class PageObject<T> implements Serializable {
	private static final long serialVersionUID = 1684876792546164773L;
	/**当前页码值*/
	private Integer pageCurrent;
	/**总页数*/
	private Integer pageCount;
	/**总记录数*/
	private Integer rowCount;
	/**记录页面大小*/
	private Integer pageSize;
	/**记录从数据库查询到的当前页记录*/
	private List<T> records;
	public PageObject() {}
	
	public PageObject(Integer pageCurrent, Integer rowCount, Integer pageSize, List<T> records) {
		super();
		this.pageCurrent = pageCurrent;
		this.rowCount = rowCount;
		this.pageSize = pageSize;
		this.records = records;
		//计算总页数
//		this.pageCount=rowCount/pageSize;
//		if(rowCount%pageSize!=0) {
//			pageCount++;
//		}
		//方法2
		this.pageCount=(rowCount-1)/pageSize+1;
	}
}
