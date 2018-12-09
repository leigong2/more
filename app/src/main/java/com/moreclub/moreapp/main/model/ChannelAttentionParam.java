package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/7/27.
 */

public class ChannelAttentionParam {

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private long friendId;
    private long ownerId;
    private String remark;

}
