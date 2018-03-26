package com.soho.mybatis.pageable.imp;

import com.soho.mybatis.pageable.Pagination;

import java.util.List;

@SuppressWarnings("serial")
public class SimplePagination<T> implements Pagination<T> {

    private Integer pageNo;
    private Integer pageSize;
    private Integer pageTotal;
    private Integer pageNumber;

    private List<T> data;

    public SimplePagination() {

    }

    public SimplePagination(Integer pageNo, Integer pageSize) {
        this(pageNo, pageSize, 0, null, null);
    }

    public SimplePagination(Integer pageNo, Integer pageSize, Integer pageTotal) {
        this(pageNo, pageSize, pageTotal, (pageTotal % pageSize == 0) ? (pageTotal / pageSize) : (pageTotal / pageSize + 1), null);
    }

    public SimplePagination(Integer pageNo, Integer pageSize, Integer pageTotal, List<T> data) {
        this(pageNo, pageSize, pageTotal, (pageTotal % pageSize == 0) ? (pageTotal / pageSize) : (pageTotal / pageSize + 1), data);
    }

    public SimplePagination(Integer pageNo, Integer pageSize, Integer pageTotal, Integer pageNumber, List<T> data) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
        this.pageNumber = pageNumber;
        this.data = data;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
