package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-07-21-0021.
 */

public class UpdateBody {
    private int versionCode;
    private int platform;

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
