package com.soho.mybatis.pageable;

import java.io.Serializable;
import java.util.List;

public interface Pagination<T> extends Serializable {

	public Integer getPageNo();

	public void setPageNo(Integer pageNo);

	public Integer getPageSize();

	public void setPageSize(Integer pageSize);

	public Integer getPageTotal();

	public void setPageTotal(Integer pageTotal);

	public Integer getPageNumber();

	public void setPageNumber(Integer pageNumber);

	public List<T> getData();

	public void setData(List<T> data);

}
