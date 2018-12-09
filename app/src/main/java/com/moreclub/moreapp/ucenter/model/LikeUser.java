package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-08-25-0025.
 */

public class LikeUser {
    private long mUid;
    private long sUid;
    private boolean state;

    public long getmUid() {
        return mUid;
    }

    public void setmUid(long mUid) {
        this.mUid = mUid;
    }

    public long getsUid() {
        return sUid;
    }

    public void setsUid(long sUid) {
        this.sUid = sUid;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
