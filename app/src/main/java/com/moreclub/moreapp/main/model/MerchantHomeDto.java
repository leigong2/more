package com.moreclub.moreapp.main.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MerchantHomeDto {
    private Integer merchantId;
    private String merchantName;
    private String thumb;
    private String tags ;
    private int hot;
    private Integer  activityId                ;//
    private String  activityTitle          ;//
    private String  activityBannerPhoto            ;//
    private Integer activityStatus;
    private Integer activityType;
    private Date shelveDate;
    private double lng;
    private double lat;
    private List<CardBean> cardList;

    public List<CardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardBean> cardList) {
        this.cardList = cardList;
    }

    public static class CardBean {

        /**
         * cardType : 1
         * cardRate : 0.9
         */

        private int cardType;
        private double cardRate;

        public int getCardType() {
            return cardType;
        }

        public void setCardType(int cardType) {
            this.cardType = cardType;
        }

        public double getCardRate() {
            return cardRate;
        }

        public void setCardRate(double cardRate) {
            this.cardRate = cardRate;
        }
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityBannerPhoto() {
        return activityBannerPhoto;
    }

    public void setActivityBannerPhoto(String activityBannerPhoto) {
        this.activityBannerPhoto = activityBannerPhoto;
    }

    public Integer getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(Integer activityStatus) {
        this.activityStatus = activityStatus;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public Date getShelveDate() {
        return shelveDate;
    }

    public void setShelveDate(Date shelveDate) {
        this.shelveDate = shelveDate;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
