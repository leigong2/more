package com.moreclub.moreapp.information.model;

import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

/**
 * Created by Administrator on 2017-05-16.
 */

public class NewStore {
    private List<MerchantItem> list;
    private int pages;
    private int total;

    public List<MerchantItem> getList() {
        return list;
    }

    public void setList(List<MerchantItem> list) {
        this.list = list;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Headline{" +
                "total=" + total +
                ", list=" + list +
                '}';
    }
}
