package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017/2/28.
 */

public class Activity {
    private Integer activityId;
    private Integer status;
    private String title;
    private String introduction;
    private String featuredDrink;
    private Integer merchantId;
    private String merchantName;
    private String bannerPhoto;
    private Integer type;
    private Long createTime;
    private Long endTime;
    private int holdingType;
    private String holdingTimes;
    private long startTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getHoldingType() {
        return holdingType;
    }

    public void setHoldingType(int holdingType) {
        this.holdingType = holdingType;
    }

    public String getHoldingTimes() {
        return holdingTimes;
    }

    public void setHoldingTimes(String holdingTimes) {
        this.holdingTimes = holdingTimes;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFeaturedDrink() {
        return featuredDrink;
    }

    public void setFeaturedDrink(String featuredDrink) {
        this.featuredDrink = featuredDrink;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getBannerPhoto() {
        return bannerPhoto;
    }

    public void setBannerPhoto(String bannerPhoto) {
        this.bannerPhoto = bannerPhoto;
    }

//    public Timestamp getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(Timestamp startTime) {
//        this.startTime = startTime;
//    }
//
//    public Timestamp getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(Timestamp endTime) {
//        this.endTime = endTime;
//    }
//
//    public Timestamp getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(Timestamp createTime) {
//        this.createTime = createTime;
//    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
