package com.moreclub.moreapp.ucenter.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/18.
 */

public class PackageCheck {
//    "pageNum":1,"pageSize":10,"size":10,"orderBy":"create_time desc","startRow":1,"endRow":10,"total":10,"pages":1,
// "list"

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

    public ArrayList<PackageCheckItem> getList() {
        return list;
    }

    public void setList(ArrayList<PackageCheckItem> list) {
        this.list = list;
    }

    private int pageNum;
    private int pageSize;
    private int size;
    private int total;
    private int pages;
    private ArrayList<PackageCheckItem> list;



}
