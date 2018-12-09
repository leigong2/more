package com.moreclub.moreapp.ucenter.model;

import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/12.
 */

public class CooperationMerchant {

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean getIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

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

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(ArrayList<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private int navigatePages;
    private int nextPage;
    private String orderBy;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;
    private int endRow;
    private int startRow;
    private int total;
    private ArrayList<Integer> navigatepageNums;

    public ArrayList<MerchantItem> getList() {
        return list;
    }

    public void setList(ArrayList<MerchantItem> list) {
        this.list = list;
    }

    private ArrayList<MerchantItem> list;
//    endRow = 12;
//    firstPage = 1;
//    hasNextPage = 0;
//    hasPreviousPage = 0;
//    isFirstPage = 1;
//    isLastPage = 1;
//    lastPage = 1;
//    navigatePages = 8;
//    navigatepageNums =     (
//            1
//            );
//    nextPage = 0;
//    orderBy = "<null>";
//    pageNum = 1;
//    pageSize = 20;
//    pages = 1;
//    prePage = 0;
//    size = 12;
//    startRow = 1;
//    total = 12;
}
