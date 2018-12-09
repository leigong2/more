package com.moreclub.moreapp.information.model;

/**
 * Created by Administrator on 2017-06-01.
 */

public class UserWriteMessageWall {

    /**
     * activityId : 0
     * appVersion : string
     * comment : string
     * parentId : 0
     * replyUid : 0
     * sign : string
     * timestamp : 0
     * uid : 0
     */

    private int activityId;
    private String appVersion;
    private String comment;
    private int parentId;
    private Long replyUid;
    private String sign;
    private Integer timestamp;
    private Long uid;

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Long getReplyUid() {
        return replyUid;
    }

    public void setReplyUid(Long replyUid) {
        this.replyUid = replyUid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
