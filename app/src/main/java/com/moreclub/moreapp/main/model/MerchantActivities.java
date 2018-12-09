package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantActivities {

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getBannerPhoto() {
        return bannerPhoto;
    }

    public void setBannerPhoto(String bannerPhoto) {
        this.bannerPhoto = bannerPhoto;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getFeaturedDrink() {
        return featuredDrink;
    }

    public void setFeaturedDrink(String featuredDrink) {
        this.featuredDrink = featuredDrink;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int activityId;
    private String bannerPhoto;
    private boolean collected;
    private String createTime;
    private String endTime;
    private int merchantId;
    private  String featuredDrink;
    private  String introduction;
    private  String merchantName;
    private String startTime;
    private int status;
    private String title;
    private int type;

}
