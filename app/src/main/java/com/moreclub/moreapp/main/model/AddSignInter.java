package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-06-20.
 */

public class AddSignInter {

    /**
     * content : string
     * gift : 0
     * mid : 0
     * type : 0
     * uid : 0
     */

    private String content;
    private String gift;
    private int mid;
    private int type;
    private Long uid;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
