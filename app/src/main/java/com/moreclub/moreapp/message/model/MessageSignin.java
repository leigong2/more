package com.moreclub.moreapp.message.model;

/**
 * Created by Captain on 2017/5/26.
 */

public class MessageSignin {


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isSetting() {
        return setting;
    }

    public void setSetting(boolean setting) {
        this.setting = setting;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    private int sid;
    private double lng;
    private double lat;
    private String merchantName;
    private int personCount;
    private boolean auto;

    public Boolean getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Boolean privacy) {
        this.privacy = privacy;
    }

    private Boolean privacy;
    private boolean setting;
    private long timestamp;
    private long uid;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    private int mid;

//    auto = 1;
//    privacy = 0;
//    setting = 1;
//    timestamp = 1495781236000;
//    uid = 3097832504509440;

//    "sid":50,"uid":70368779844,"auto":true,"mid":74,"setting":false,"timestamp":1495711063000,"lng":104.067466,"lat":30.546317,"merchantName":"汤姆兰德啤酒屋","personCount":1
}
