package com.moreclub.moreapp.packages.model.event;

/**
 * Created by Captain on 2017/4/25.
 */

public class PkgCollectEvent {
    public boolean ismIsCollect() {
        return mIsCollect;
    }

    public void setmIsCollect(boolean mIsCollect) {
        this.mIsCollect = mIsCollect;
    }

    private boolean mIsCollect;

    public int getmPostion() {
        return mPostion;
    }

    public void setmPostion(int mPostion) {
        this.mPostion = mPostion;
    }

    private int mPostion;
    public PkgCollectEvent(boolean isCollect,int pos){
        mIsCollect = isCollect;
        mPostion = pos;
    }
}
