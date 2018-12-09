package com.moreclub.moreapp.main.model;

import com.moreclub.moreapp.ucenter.model.BaseUserDetails;

/**
 * Created by Administrator on 2017-06-02.
 */

public class MerchantActivity extends BaseUserDetails {

    /**
     * activityId : 0
     * bannerPhoto : string
     * collected : true
     * endTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
     * featuredDrink : string
     * holdingTimes : string
     * holdingType : 0
     * introduction : string
     * startTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
     * status : 0
     * title : string
     * type : 0
     */

    private int activityId;
    private String bannerPhoto;
    private boolean collected;
    private Long endTime;
    private String featuredDrink;
    private String holdingTimes;
    private int holdingType;
    private String introduction;
    private Long startTime;
    private int status;
    private String title;
    private int type;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

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

    public String getFeaturedDrink() {
        return featuredDrink;
    }

    public void setFeaturedDrink(String featuredDrink) {
        this.featuredDrink = featuredDrink;
    }

    public String getHoldingTimes() {
        return holdingTimes;
    }

    public void setHoldingTimes(String holdingTimes) {
        this.holdingTimes = holdingTimes;
    }

    public int getHoldingType() {
        return holdingType;
    }

    public void setHoldingType(int holdingType) {
        this.holdingType = holdingType;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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
}
