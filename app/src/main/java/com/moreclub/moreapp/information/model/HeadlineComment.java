package com.moreclub.moreapp.information.model;

/**
 * Created by Administrator on 2017-05-17.
 */

public class HeadlineComment {
    private int cid;
    private String content;
    private long createTime;
    private String fromNickname;
    private String fromUserThumb;
    private String toNickName;
    private long toUserId;
    private String toUserThumb;
    private long userId;
    private boolean clicked;
    private int likeTimes;

    public int getLikeTimes() {
        return likeTimes;
    }

    public void setLikeTimes(int likeTimes) {
        this.likeTimes = likeTimes;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFromNickname() {
        return fromNickname;
    }

    public void setFromNickname(String fromNickname) {
        this.fromNickname = fromNickname;
    }

    public String getFromUserThumb() {
        return fromUserThumb;
    }

    public void setFromUserThumb(String fromUserThumb) {
        this.fromUserThumb = fromUserThumb;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserThumb() {
        return toUserThumb;
    }

    public void setToUserThumb(String toUserThumb) {
        this.toUserThumb = toUserThumb;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

/*        private int brandId;
        private int cityId;
        private int colloct;
        private int brancolloctdId;
        private int del;
        private int displayModal;
        private int likeTime1;
        private int likeTime2;
        private int likeTime3;
        private int likeTime4;
        private int likeTime5;
        private int mediaType;
        private int sid;
        private int type;
        private long createTime;
        private long forwardId;
        private long uid;
        private String clicked;
        private String content;
        private String introduction;
        private String likeTimes;
        private String mediaUrl;
        private String readTimes;
        private String title;
        private String titlePicture;
        private String userthumb;
        private String username;*/
}
