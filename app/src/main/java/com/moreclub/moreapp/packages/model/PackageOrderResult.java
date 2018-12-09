package com.moreclub.moreapp.packages.model;

/**
 * Created by Captain on 2017/3/27.
 */

public class PackageOrderResult {
//    {
//        leftSecond = 1200;
//        orderId = 10310604911291;
//    }

    public long getLeftSecond() {
        return leftSecond;
    }

    public void setLeftSecond(long leftSecond) {
        this.leftSecond = leftSecond;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    private long leftSecond;
    private long orderId;

}
