package com.moreclub.moreapp.ucenter.model.event;

/**
 * Created by Administrator on 2017/3/21.
 */

public class UserLoginEvent {
    private String userAvatar;

    public UserLoginEvent(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserAvatar() {
        return userAvatar;
    }
}
