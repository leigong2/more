package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017/2/24.
 */

public class BannerItem {
    private Integer  advertisementId;
    private Long  relationId              ;//
    private String  title          ;//
    private String  bannerPhoto            ;//
    private Integer   relationType      ;//
    private Integer  clickType; //跳转类型，0活动，1资讯，2跳内部浏览器，3跳外部浏览器
    private String  clickLink;

    public Integer getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Integer advertisementId) {
        this.advertisementId = advertisementId;
    }

    public Long getRelationId() {
        return relationId;
    }

    public void setRelationId(Long relationId) {
        this.relationId = relationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerPhoto() {
        return bannerPhoto;
    }

    public void setBannerPhoto(String bannerPhoto) {
        this.bannerPhoto = bannerPhoto;
    }

    public Integer getRelationType() {
        return relationType;
    }

    public void setRelationType(Integer relationType) {
        this.relationType = relationType;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }

    public String getClickLink() {
        return clickLink;
    }

    public void setClickLink(String clickLink) {
        this.clickLink = clickLink;
    }

    @Override
    public String toString() {
        return "BannerItem{" +
                "advertisementId=" + advertisementId +
                ", relationId=" + relationId +
                ", title='" + title + '\'' +
                ", bannerPhoto='" + bannerPhoto + '\'' +
                ", relationType=" + relationType +
                '}';
    }
}
