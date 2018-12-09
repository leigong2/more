package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-07-21-0021.
 */

public class UpdateResp {

    /**
     * update : true
     * forceUpdate : false
     * downloadUrl : https://www.moreclub.cn/v1/client/open/download
     * updateDesc :
     * versionCode : 25
     * versionName : v1.0.1
     */

    private boolean update;
    private boolean forceUpdate;
    private String downloadUrl;
    private String updateDesc;
    private int versionCode;
    private String versionName;

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUpdateDesc() {
        return updateDesc;
    }

    public void setUpdateDesc(String updateDesc) {
        this.updateDesc = updateDesc;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
