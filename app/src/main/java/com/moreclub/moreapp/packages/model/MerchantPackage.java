package com.moreclub.moreapp.packages.model;


import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/13.
 */

public class MerchantPackage {

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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public ArrayList<PackageBaseInfo> getList() {
        return list;
    }

    public void setList(ArrayList<PackageBaseInfo> list) {
        this.list = list;
    }
    private int pageNum;
    private int pageSize;
    private int pages;
    private ArrayList<PackageBaseInfo> list;
}
