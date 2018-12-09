package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-05-24.
 */

public class HeadlineCount {

    /**
     * infoCount : 0
     * merchantCount : 0
     * searchHint : string
     * serverTime : 0
     */

    private int infoCount;
    private int merchantCount;
    private String searchHint;
    private int serverTime;

    public int getInfoCount() {
        return infoCount;
    }

    public void setInfoCount(int infoCount) {
        this.infoCount = infoCount;
    }

    public int getMerchantCount() {
        return merchantCount;
    }

    public void setMerchantCount(int merchantCount) {
        this.merchantCount = merchantCount;
    }

    public String getSearchHint() {
        return searchHint;
    }

    public void setSearchHint(String searchHint) {
        this.searchHint = searchHint;
    }

    public int getServerTime() {
        return serverTime;
    }

    public void setServerTime(int serverTime) {
        this.serverTime = serverTime;
    }

    @Override
    public String toString() {
        return "MerchantCount{" +
                "infoCount=" + infoCount +
                ", merchantCount=" + merchantCount +
                ", searchHint='" + searchHint + '\'' +
                ", serverTime=" + serverTime +
                '}';
    }
}
