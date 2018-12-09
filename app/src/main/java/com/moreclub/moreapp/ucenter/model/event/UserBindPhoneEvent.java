package com.moreclub.moreapp.ucenter.model.event;

/**
 * Created by Administrator on 2017-05-22.
 */

public class UserBindPhoneEvent {
    private String phone;

    public UserBindPhoneEvent(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
