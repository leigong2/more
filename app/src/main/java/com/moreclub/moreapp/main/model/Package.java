package com.moreclub.moreapp.main.model;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Package {
    private Integer pid;
    private Integer type;
    private Integer status;
    private String title;
    private BigDecimal price;
    private Integer personNum;
    private BigDecimal marketPrice;
    private String photos;
    private Integer merchantId;
    private String merchantName;
    private int avlInventory;
    private int sellDays;
    private BigDecimal oldPrice;
    private Double lng;
    private Double lat;
    private boolean collected;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPersonNum() {
        return personNum;
    }

    public void setPersonNum(Integer personNum) {
        this.personNum = personNum;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
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

    public int getAvlInventory() {
        return avlInventory;
    }

    public void setAvlInventory(int avlInventory) {
        this.avlInventory = avlInventory;
    }

    public int getSellDays() {
        return sellDays;
    }

    public void setSellDays(int sellDays) {
        this.sellDays = sellDays;
    }

    public BigDecimal getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(BigDecimal oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
