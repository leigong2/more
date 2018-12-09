package com.moreclub.moreapp.information.model;

/**
 * Created by Administrator on 2017-06-21.
 */

public class SignInter {

    /**
     * fromUid : 0
     * sid : 0
     * title : string
     * uid : 0
     */

    private Long fromUid;
    private int sid;
    private String title;
    private Long uid;

    public Long getFromUid() {
        return fromUid;
    }

    public void setFromUid(Long fromUid) {
        this.fromUid = fromUid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
