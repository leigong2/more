package com.moreclub.moreapp.packages.model;

import java.util.List;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageList {


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<PackageBaseInfo> getList() {
        return list;
    }

    public void setList(List<PackageBaseInfo> list) {
        this.list = list;
    }

    private int pageNum;
    private int pageSize;
    private int size;
    private int orderBy;
    private int startRow;
    private int endRow;
    private int total;
    private int pages;
    private List<PackageBaseInfo> list;
}
