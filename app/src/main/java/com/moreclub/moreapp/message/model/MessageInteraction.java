package com.moreclub.moreapp.message.model;

/**
 * Created by Administrator on 2017-06-22.
 */

public class MessageInteraction {
    private Boolean inviteOrInter;
    private String merchantName;
    private Integer mid;
    private String nickName;
    private Integer sid;
    private Integer status;
    private String thumb;
    private Long timestamp;
    private String title;
    private Integer type;
    private Long uid;
    private Integer vid;

    public Boolean getInviteOrInter() {
        return inviteOrInter;
    }

    public void setInviteOrInter(Boolean inviteOrInter) {
        this.inviteOrInter = inviteOrInter;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }
}
