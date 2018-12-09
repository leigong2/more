package com.moreclub.moreapp.main.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-06-20.
 */

public class MerchantsSignInters implements Serializable{

    /**
     * age : 0
     * colseTime : 0
     * content : string
     * deled : true
     * exprise : 0
     * gender : string
     * gift : 0
     * merchantName : string
     * mid : 0
     * nickName : string
     * sid : 0
     * timestamp : 0
     * type : 0
     * uid : 0
     * userThumb : string
     */

    private int age;
    private int colseTime;
    private String content;
    private boolean deled;
    private int exprise;
    private String gender;
    private int gift;
    private String merchantName;
    private int mid;
    private String nickName;
    private int sid;
    private long timestamp;
    private int type;
    private Long uid;
    private String userThumb;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getColseTime() {
        return colseTime;
    }

    public void setColseTime(int colseTime) {
        this.colseTime = colseTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDeled() {
        return deled;
    }

    public void setDeled(boolean deled) {
        this.deled = deled;
    }

    public int getExprise() {
        return exprise;
    }

    public void setExprise(int exprise) {
        this.exprise = exprise;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
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

    public String getUserThumb() {
        return userThumb;
    }

    public void setUserThumb(String userThumb) {
        this.userThumb = userThumb;
    }
}
