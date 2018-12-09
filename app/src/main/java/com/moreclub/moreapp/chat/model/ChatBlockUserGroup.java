package com.moreclub.moreapp.chat.model;

/**
 * Created by Captain on 2017/6/12.
 */

public class ChatBlockUserGroup {

    public String getBlockID() {
        return blockID;
    }

    public void setBlockID(String blockID) {
        this.blockID = blockID;
    }

    public boolean getBlockStatus() {
        return blockStatus;
    }

    public void setBlockStatus(boolean blockStatus) {
        this.blockStatus = blockStatus;
    }

    private String blockID;
    private boolean blockStatus;

}
