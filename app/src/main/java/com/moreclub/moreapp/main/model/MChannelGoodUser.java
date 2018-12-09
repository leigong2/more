package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-07-28-0028.
 */

public class MChannelGoodUser {

    /**
     * clicked : likeTime1
     * clicktime : 1501243360000
     * fid : 125
     * nickName : 涛涛
     * uid : 123544382468
     */

    private String clicked;
    private long clicktime;
    private int fid;
    private String nickName;
    private String thumb;
    private long uid;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getClicked() {
        return clicked;
    }

    public void setClicked(String clicked) {
        this.clicked = clicked;
    }

    public long getClicktime() {
        return clicktime;
    }

    public void setClicktime(long clicktime) {
        this.clicktime = clicktime;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
