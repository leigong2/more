package com.moreclub.moreapp.main.model.event;

import com.moreclub.moreapp.information.model.HeadlineComment;

/**
 * Created by Administrator on 2017-08-01-0001.
 */

public class MChannelCommentEvent {
    private HeadlineComment comment;
    private Boolean isClicked;
    private int sid;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public Boolean isClicked() {
        return isClicked;
    }

    public void setClicked(Boolean clicked) {
        isClicked = clicked;
    }

    public HeadlineComment getComment() {
        return comment;
    }

    public void setComment(HeadlineComment comment) {
        this.comment = comment;
    }
}
