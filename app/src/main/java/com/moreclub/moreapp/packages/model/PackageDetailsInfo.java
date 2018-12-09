package com.moreclub.moreapp.packages.model;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/3/23.
 */

public class PackageDetailsInfo {

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppointmentRemark() {
        return appointmentRemark;
    }

    public void setAppointmentRemark(String appointmentRemark) {
        this.appointmentRemark = appointmentRemark;
    }

    public String getAvlEndDate() {
        return avlEndDate;
    }

    public void setAvlEndDate(String avlEndDate) {
        this.avlEndDate = avlEndDate;
    }

    public int getAvlInventory() {
        return avlInventory;
    }

    public void setAvlInventory(int avlInventory) {
        this.avlInventory = avlInventory;
    }

    public String getAvlRemark() {
        return avlRemark;
    }

    public void setAvlRemark(String avlRemark) {
        this.avlRemark = avlRemark;
    }

    public String getAvlStartDate() {
        return avlStartDate;
    }

    public void setAvlStartDate(String avlStartDate) {
        this.avlStartDate = avlStartDate;
    }

    public String getAvlTime() {
        return avlTime;
    }

    public void setAvlTime(String avlTime) {
        this.avlTime = avlTime;
    }

    public boolean getCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public ArrayList<PackageDetailsSubContent> getContents() {
        return contents;
    }

    public void setContents(ArrayList<PackageDetailsSubContent> contents) {
        this.contents = contents;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean getDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public int getDrawback() {
        return drawback;
    }

    public void setDrawback(int drawback) {
        this.drawback = drawback;
    }

    public String getDrawbackRemark() {
        return drawbackRemark;
    }

    public void setDrawbackRemark(String drawbackRemark) {
        this.drawbackRemark = drawbackRemark;
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

    public String getMerchantPhone() {
        return merchantPhone;
    }

    public void setMerchantPhone(String merchantPhone) {
        this.merchantPhone = merchantPhone;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSellDays() {
        return sellDays;
    }

    public void setSellDays(int sellDays) {
        this.sellDays = sellDays;
    }

    public String getSellEndDate() {
        return sellEndDate;
    }

    public void setSellEndDate(String sellEndDate) {
        this.sellEndDate = sellEndDate;
    }

    public String getSellStartDate() {
        return sellStartDate;
    }

    public void setSellStartDate(String sellStartDate) {
        this.sellStartDate = sellStartDate;
    }

    public int getSoldInventory() {
        return soldInventory;
    }

    public void setSoldInventory(int soldInventory) {
        this.soldInventory = soldInventory;
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

    public int getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(int totalInventory) {
        this.totalInventory = totalInventory;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUseRules() {
        return useRules;
    }

    public void setUseRules(String useRules) {
        this.useRules = useRules;
    }

    private String address;
    private String appointmentRemark;
    private String avlEndDate;
    private int avlInventory;
    private String avlRemark;
    private String avlStartDate;
    private String  avlTime;
    private boolean collected;
    private ArrayList<PackageDetailsSubContent> contents;
    private long createTime;
    private boolean del;
    private int drawback;
    private String drawbackRemark;
    private String lat;
    private String lng;
    private String marketPrice;
    private int merchantId;
    private String merchantName;
    private String merchantPhone;
    private float oldPrice;
    private int personNum;
    private String photos;
    private  int pid;
    private double price;
    private int sellDays;
    private  String sellEndDate;
    private  String sellStartDate;
    private  int soldInventory;
    private  int status;
    private  String title;
    private  int totalInventory;
    private int type;
    private  long uid;
    private  long updateTime;
    private  String useRules;

//    Printing description of data:
//    {
//        address = "\U4eba\U6c11\U5357\U8def4\U6bb548\U53f7\U964413\U53f7";
//        appointmentRemark = "\U8bf7\U63d0\U524d:0 \U5929 2\U5c0f\U65f6\U9884\U7ea6";
//        avlEndDate = "2017-03-11";
//        avlInventory = 10;
//        avlRemark = "";
//        avlStartDate = "2017-01-11";
//        avlTime = "19:00-2:00";
//        collected = 0;
//        contents =     (
//                {
//                        content = "\U4efd\U996d,\U5e7f\U544a";
//        option = "2\U90092";
//        pid = 3;
//        productType = 2;
//        typeRemark = "\U6d0b\U9152";
//        }
//        );
//        createTime = 1484115737000;
//        del = 0;
//        drawback = 1;
//        drawbackRemark = "<null>";
//        lat = "30.622";
//        lng = "104.068";
//        marketPrice = "<null>";
//        merchantId = 53;
//        merchantName = "Finity bar";
//        merchantPhone = "028-62988913";
//        oldPrice = "<null>";
//        personNum = 1;
//        photos = "http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/UpotkjbjZt9y52HrOHzr1T8CQ.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/LQTNCkexwBldY8mgl0Oqmd6HY.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/11/DJfFHhwqTYDnsw14QwiOPb6bR.jpg;http://more-info.oss-cn-beijing.aliyuncs.com/2017/01/23/mJdvZD98i8ASA7YtEMbspSPHm.jpg";
//        pid = 3;
//        price = 150;
//        sellDays = 1;
//        sellEndDate = "2017-03-11";
//        sellStartDate = "2017-01-11";
//        soldInventory = 0;
//        status = 1;
//        title = "\U82cf\U683c\U5170\U4e94\U5927\U4ea7\U533a\U5a01\U58eb\U5fcc\U54c1\U9274\U5957\U9910";
//        totalInventory = 10;
//        type = 1;
//        uid = 3005765457822721;
//        updateTime = 1485153112000;
//        useRules = "";
//    }

}
