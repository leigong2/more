package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/7/28.
 */

public class UserLikesItem {

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    private long uid;
    private String nickName;
    private String thumb;


}
