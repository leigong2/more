package com.moreclub.moreapp.information.model;

/**
 * Created by Administrator on 2017-05-17.
 */

public class HeadlineSendComment {

    private String appVersion;
    private String content;
    private int postId;
    private String refCommentId;
    private int timestamp;
    private long toUserId;
    private long uid;
    private String toNickName;

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getRefCommentId() {
        return refCommentId;
    }

    public void setRefCommentId(String refCommentId) {
        this.refCommentId = refCommentId;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
