package com.moreclub.moreapp.ucenter.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class OrderResp {
    private int pages;
    private List<Order> list;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }
}
