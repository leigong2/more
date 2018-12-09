package com.moreclub.moreapp.packages.model;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageBaseInfo {

//    avlInventory = 10;
//    collected = "<null>";
//    lat = "30.622";
//    lng = "104.068";
//    marketPrice = "<null>";
//    merchantId = 53;
//    merchantName = "Finity bar";
//    oldPrice = "<null>";
//    personNum = 1;
//    photos = "http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/UpotkjbjZt9y52HrOHzr1T8CQ.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/LQTNCkexwBldY8mgl0Oqmd6HY.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/DJfFHhwqTYDnsw14QwiOPb6bR.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/23/mJdvZD98i8ASA7YtEMbspSPHm.jpg";
//    pid = 3;
//    price = 150;
//    sellDays = 1;
//    status = 1;
//    title = "\U82cf\U683c\U5170\U4e94\U5927\U4ea7\U533a\U5a01\U58eb\U5fcc\U54c1\U9274\U5957\U9910";
//    type = 1;
//    appointmentRemark = "\U65e0\U9700\U9884\U7ea6";

    private int avlInventory;
    private boolean collected;
    private String lat;
    private String lng;
    private String marketPrice;
    private int merchantId;
    private String merchantName;
    private float oldPrice;
    private int personNum;
    private String photos;
    private int pid;
    private float price;
    private int  sellDays;
    private int status;
    private String title;
    private int type;
    private String appointmentRemark;

    public String getAppointmentRemark() {
        return appointmentRemark;
    }

    public void setAppointmentRemark(String appointmentRemark) {
        this.appointmentRemark = appointmentRemark;
    }
    public int getAvlInventory() {
        return avlInventory;
    }

    public void setAvlInventory(int avlInventory) {
        this.avlInventory = avlInventory;
    }

    public boolean getCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
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

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getPersonNum() {
        return personNum;
    }

    public void setPersonNum(int personNum) {
        this.personNum = personNum;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getSellDays() {
        return sellDays;
    }

    public void setSellDays(int sellDays) {
        this.sellDays = sellDays;
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
