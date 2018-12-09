package com.moreclub.moreapp.main.model;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/5/4.
 */

public class SplashImage {
    private String  title;
    private String  imageUrl;
    private Integer  clickType;//
    private String  clickLink;
    private Long expire;//

    public String getClickLink() {
        return clickLink;
    }

    public void setClickLink(String clickLink) {
        this.clickLink = clickLink;
    }

    public Integer getClickType() {
        return clickType;
    }

    public void setClickType(Integer clickType) {
        this.clickType = clickType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }
}
