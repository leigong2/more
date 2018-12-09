package com.moreclub.moreapp.main.model;

import com.hyphenate.chat.EMConversation;

import java.util.Map;

/**
 * Created by Captain on 2017/5/3.
 */

public class MessageBase {

    public String getPushType() {
        return pushType;
    }

    public void setPushType(String pushType) {
        this.pushType = pushType;
    }

    public String getPushModel() {
        return pushModel;
    }

    public void setPushModel(String pushModel) {
        this.pushModel = pushModel;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getPushDes() {
        return pushDes;
    }

    public void setPushDes(String pushDes) {
        this.pushDes = pushDes;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }

    private String pushType;
    private String pushModel;
    private String messageType;
    private String pushDes;
    private String pushTitle;
    private long pushTime;
    private Map<?,?>map;
    private int unReadCount;

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Map<?, ?> getMap() {
        return map;
    }

    public void setMap(Map<?, ?> map) {
        this.map = map;
    }

    public EMConversation getConversation() {
        return conversation;
    }

    public void setConversation(EMConversation conversation) {
        this.conversation = conversation;
    }

    private EMConversation conversation;


}
