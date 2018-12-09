package com.moreclub.moreapp.information.model;

/**
 * Created by Captain on 2017/6/8.
 */

public class ChatGroupAdd {
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String groupId;
    private String userId;
}
