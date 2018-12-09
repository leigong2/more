package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Captain on 2017/2/27.
 */

public class MerchantItem {

//    {
//        "mid":126, "name":"赋格 Fugue Bar", "address":"紫竹北街85号大世界商业广场内1-138", "logo":null, "thumb":
//        "http://more-activity.oss-cn-beijing.aliyuncs.com/2016/12/29/6NMXMRDYmyOlZ2EXKpRnfb0AZ.jpg", "distance":
//        null, "lng":104.061332, "lat":30.622084, "referencePrice":null, "tags":
//        "威士忌,JAZZ,ART", "busName":"桐梓林-玉林", "recommendReason":"音乐酒馆", "recentActNum":
//        0, "sellingPoint":"老牌Speakeasy酒吧", "shelveDate":null, "hot":0, "activities":
//        null, "featuredDrink":"威士忌", "featuredMusic":"JAZZ", "featuredScene":"ART", "level":
//        null, "cityCode":"cd", "aliasName":"赋格酒吧", "collected":null
//    }

    private Integer mid;
    private String  name;
    private String address;
    private String logo;
    private String thumb;
    private String distance;
    private double lng;
    private double lat;
    private Float referencePrice;
    private String  busName;
    private String recommendReason;
    private Integer recentActNum;
    private String  sellingPoint;
    private String shelveDate;
    private Integer hot;
    private List<Activity> activities;
    private String featuredDrink;
    private String featuredMusic;
    private String featuredScene;
    private Integer level;
    private String cityCode;
    private String aliasName;
    private Integer collected;
    private String distanceResult;
    private String tags;
    private List<MerchantHomeDto.CardBean> cardList;

    public Boolean getSupportCoupon() {
        return supportCoupon;
    }

    public void setSupportCoupon(Boolean supportCoupon) {
        this.supportCoupon = supportCoupon;
    }

    private  Boolean supportCoupon;


    public List<MerchantHomeDto.CardBean> getCardList() {
        return cardList;
    }

    public void setCardList(List<MerchantHomeDto.CardBean> cardList) {
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;

    public String getDistanceResult() {
        return distanceResult;
    }

    public void setDistanceResult(String distanceResult) {
        this.distanceResult = distanceResult;
    }



    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public Float getReferencePrice() {
        return referencePrice;
    }

    public void setReferencePrice(Float referencePrice) {
        this.referencePrice = referencePrice;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getRecommendReason() {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason) {
        this.recommendReason = recommendReason;
    }

    public Integer getRecentActNum() {
        return recentActNum;
    }

    public void setRecentActNum(Integer recentActNum) {
        this.recentActNum = recentActNum;
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

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public String getFeaturedDrink() {
        return featuredDrink;
    }

    public void setFeaturedDrink(String featuredDrink) {
        this.featuredDrink = featuredDrink;
    }

    public String getFeaturedMusic() {
        return featuredMusic;
    }

    public void setFeaturedMusic(String featuredMusic) {
        this.featuredMusic = featuredMusic;
    }

    public String getFeaturedScene() {
        return featuredScene;
    }

    public void setFeaturedScene(String featuredScene) {
        this.featuredScene = featuredScene;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }


    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    @Override
    public String toString() {
        return "MerchantItem{" +
                "mid=" + mid +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", logo='" + logo + '\'' +
                ", thumb='" + thumb + '\'' +
                ", distance='" + distance + '\'' +
                ", lng=" + lng +
                ", lat=" + lat +
                ", referencePrice=" + referencePrice +
                ", busName='" + busName + '\'' +
                ", recommendReason='" + recommendReason + '\'' +
                ", recentActNum=" + recentActNum +
                ", sellingPoint='" + sellingPoint + '\'' +
                ", shelveDate='" + shelveDate + '\'' +
                ", hot=" + hot +
                ", activities=" + activities +
                ", featuredDrink='" + featuredDrink + '\'' +
                ", featuredMusic='" + featuredMusic + '\'' +
                ", featuredScene='" + featuredScene + '\'' +
                ", level=" + level +
                ", cityCode='" + cityCode + '\'' +
                ", aliasName='" + aliasName + '\'' +
                ", collected=" + collected +
                ", distanceResult='" + distanceResult + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
