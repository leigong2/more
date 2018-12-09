package com.moreclub.moreapp.information.model;

import java.util.List;

/**
 * Created by Administrator on 2017-06-01.
 */

public class MessageWall {

    /**
     * activityId : 0
     * comment : string
     * commentId : 0
     * createTime : {"date":0,"day":0,"hours":0,"minutes":0,"month":0,"nanos":0,"seconds":0,"time":0,"timezoneOffset":0,"year":0}
     * likeTimes : 0
     * parentId : 0
     * read : true
     * replyNum : 0
     * replyUid : 0
     * uid : 0
     * userName : string
     * userThumb : string
     */

    private int activityId;
    private String comment;
    private int commentId;
    private Long createTime;
    private int likeTimes;
    private int parentId;
    private boolean read;
    private int replyNum;
    private int replyUid;
    private Long uid;
    private String userName;
    private String userThumb;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(int likeTimes) {
        this.likeTimes = likeTimes;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    public int getReplyUid() {
        return replyUid;
    }

    public void setReplyUid(int replyUid) {
        this.replyUid = replyUid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserThumb() {
        return userThumb;
    }

    public void setUserThumb(String userThumb) {
        this.userThumb = userThumb;
    }
}
