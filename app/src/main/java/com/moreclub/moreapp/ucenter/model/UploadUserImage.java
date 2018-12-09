package com.moreclub.moreapp.ucenter.model;

import java.util.List;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

public class UploadUserImage {
    private long uid;
    private List<String> urls;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
