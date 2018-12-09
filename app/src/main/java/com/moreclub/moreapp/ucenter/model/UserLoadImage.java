package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

public class UserLoadImage {

    /**
     * ctime : 0
     * id : 0
     * thumbUrl : string
     * uid : 0
     * url : string
     */

    private long ctime;
    private long id;
    private String thumbUrl;
    private long uid;
    private String url;

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
