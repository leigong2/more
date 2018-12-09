package com.moreclub.moreapp.main.model;

/**
 * Created by Administrator on 2017-06-26.
 */

public class InterInvites {

    /**
     * vid : 138
     * sid : 228
     * status : 4
     * timestamp : 1498466075
     * title : 您好,我对您的拼座感兴趣噢!
     * inviteOrInter : false
     * type : 5
     * merchantName : 汤姆兰德啤酒屋
     * mid : 74
     * uid : 989270562948
     * nickName : null
     * thumb : null
     */

    private int vid;
    private int sid;
    private int status;
    private Long timestamp;
    private String title;
    private boolean inviteOrInter;
    private int type;
    private String merchantName;
    private int mid;
    private Long uid;
    private String nickName;
    private String thumb;

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isInviteOrInter() {
        return inviteOrInter;
    }

    public void setInviteOrInter(boolean inviteOrInter) {
        this.inviteOrInter = inviteOrInter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
