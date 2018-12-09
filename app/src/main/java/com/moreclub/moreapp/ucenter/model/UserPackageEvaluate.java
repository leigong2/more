package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Captain on 2017/4/1.
 */

public class UserPackageEvaluate {
//    {
//        "buyerPhotos": "string",
//            "facilityStar": 0,
//            "musicStar": 0,
//            "orderId": 0,
//            "otherComment": "string",
//            "pStar": 0,
//            "pid": 0,
//            "sceneStar": 0,
//            "uid": 0
//    }

    public String getBuyerPhotos() {
        return buyerPhotos;
    }

    public void setBuyerPhotos(String buyerPhotos) {
        this.buyerPhotos = buyerPhotos;
    }

    public String getOtherComment() {
        return otherComment;
    }

    public void setOtherComment(String otherComment) {
        this.otherComment = otherComment;
    }

    public float getFacilityStar() {
        return facilityStar;
    }

    public void setFacilityStar(float facilityStar) {
        this.facilityStar = facilityStar;
    }

    public float getMusicStar() {
        return musicStar;
    }

    public void setMusicStar(float musicStar) {
        this.musicStar = musicStar;
    }

    public float getpStar() {
        return pStar;
    }

    public void setpStar(float pStar) {
        this.pStar = pStar;
    }

    public float getSceneStar() {
        return sceneStar;
    }

    public void setSceneStar(float sceneStar) {
        this.sceneStar = sceneStar;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    private String buyerPhotos;
    private String otherComment;
    private float facilityStar;
    private float musicStar;
    private float pStar;
    private float sceneStar;
    private long orderId;
    private long uid;
    private int pid;

}
