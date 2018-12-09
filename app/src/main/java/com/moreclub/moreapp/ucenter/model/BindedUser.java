package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-08-08-0008.
 */

public class BindedUser {
    private String nickName;
    private String userAvatar;

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
