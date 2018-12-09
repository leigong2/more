package com.moreclub.moreapp.main.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/2/28.
 */

public class MerchantDetails {


    public ArrayList<MerchantActivities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<MerchantActivities> activities) {
        this.activities = activities;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean getCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getDrinkKinds() {
        return drinkKinds;
    }

    public void setDrinkKinds(int drinkKinds) {
        this.drinkKinds = drinkKinds;
    }

    public ArrayList<MerchantDetailsItem> getDrinkList() {
        return drinkList;
    }

    public void setDrinkList(ArrayList<MerchantDetailsItem> drinkList) {
        this.drinkList = drinkList;
    }

    public ArrayList<MerchantDetailsItem> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(ArrayList<MerchantDetailsItem> facilityList) {
        this.facilityList = facilityList;
    }

    public int getFloorSpace() {
        return floorSpace;
    }

    public void setFloorSpace(int floorSpace) {
        this.floorSpace = floorSpace;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public ArrayList<MerchantDetailsItem> getMusicList() {
        return musicList;
    }

    public void setMusicList(ArrayList<MerchantDetailsItem> musicList) {
        this.musicList = musicList;
    }

    public String getMusicPlay() {
        return musicPlay;
    }

    public void setMusicPlay(String musicPlay) {
        this.musicPlay = musicPlay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getOnlineBill() {
        return onlineBill;
    }

    public void setOnlineBill(boolean onlineBill) {
        this.onlineBill = onlineBill;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public boolean getOwnActivity() {
        return ownActivity;
    }

    public void setOwnActivity(boolean ownActivity) {
        this.ownActivity = ownActivity;
    }

    public boolean getOwnBooth() {
        return ownBooth;
    }

    public void setOwnBooth(boolean ownBooth) {
        this.ownBooth = ownBooth;
    }

    public boolean getOwnPackage() {
        return ownPackage;
    }

    public void setOwnPackage(boolean ownPackage) {
        this.ownPackage = ownPackage;
    }

    public String getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(String payMethods) {
        this.payMethods = payMethods;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public ArrayList<MerchantPromo> getPromoList() {
        return promoList;
    }

    public void setPromoList(ArrayList<MerchantPromo> promoList) {
        this.promoList = promoList;
    }

    public int getRecentActNum() {
        return recentActNum;
    }

    public void setRecentActNum(int recentActNum) {
        this.recentActNum = recentActNum;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public String getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(String referencePrice) {
        this.referencePrice = referencePrice;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public ArrayList<MerchantDetailsItem> getScenePictureList() {
        return scenePictureList;
    }

    public void setScenePictureList(ArrayList<MerchantDetailsItem> scenePictureList) {
        this.scenePictureList = scenePictureList;
    }

    public String getSellingPoint() {
        return sellingPoint;
    }

    public void setSellingPoint(String sellingPoint) {
        this.sellingPoint = sellingPoint;
    }

    public String getShelveDate() {
        return shelveDate;
    }

    public void setShelveDate(String shelveDate) {
        this.shelveDate = shelveDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private ArrayList<MerchantActivities> activities;
    private String address;
    private String busName;
    private int capacity;
    private boolean collected;
    private String distance;
    private int drinkKinds;
    private ArrayList<MerchantDetailsItem>drinkList;
    private ArrayList<MerchantDetailsItem>facilityList;
    private int floorSpace;
    private int hot;
    private String lat;
    private String lng;
    private String logo;
    private String mediaUrl;
    private int mid;
    private ArrayList<MerchantDetailsItem>musicList;
    private String musicPlay;
    private String name;
    private boolean onlineBill;
    private String openTime;
    private boolean ownActivity;
    private boolean ownBooth;
    private boolean ownPackage;
    private String payMethods;
    private String phone;
    private ArrayList<String> pictures;
    private ArrayList<MerchantPromo>promoList;
    private int recentActNum;
    private String recommendReason;
    private String referencePrice;
    private int rooms;
    private ArrayList<MerchantDetailsItem>scenePictureList;
    private String sellingPoint;
    private String shelveDate;
    private String tags;
    private String thumb;



}
