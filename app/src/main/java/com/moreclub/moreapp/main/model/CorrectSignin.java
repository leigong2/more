package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/5/25.
 */

public class CorrectSignin {

    public String getNewMid() {
        return newMid;
    }

    public void setNewMid(String newMid) {
        this.newMid = newMid;
    }

    public String getNewMname() {
        return newMname;
    }

    public void setNewMname(String newMname) {
        this.newMname = newMname;
    }

    public String getOldMid() {
        return oldMid;
    }

    public void setOldMid(String oldMid) {
        this.oldMid = oldMid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String newMid;
    private String newMname;
    private String oldMid;
    private String uid;

}
