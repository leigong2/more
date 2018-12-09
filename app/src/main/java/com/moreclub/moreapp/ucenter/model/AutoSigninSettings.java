package com.moreclub.moreapp.ucenter.model;

import retrofit2.http.Query;

/**
 * Created by Captain on 2017/5/26.
 */

public class AutoSigninSettings {

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getAutoSignin() {
        return autoSignin;
    }

    public void setAutoSignin(Boolean autoSignin) {
        this.autoSignin = autoSignin;
    }

    public Boolean getBarVisiable() {
        return barVisiable;
    }

    public void setBarVisiable(Boolean barVisiable) {
        this.barVisiable = barVisiable;
    }

    private String uid;
    private Boolean autoSignin;
    private Boolean barVisiable;

}
