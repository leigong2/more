package com.moreclub.moreapp.main.model.event;

/**
 * Created by Captain on 2017/8/28.
 */

public class ChannelPublishSuccessEvent {

    public ChannelPublishSuccessEvent(boolean success){
        channelPublishSuccess = success;
    }

    public boolean isChannelPublishSuccess() {
        return channelPublishSuccess;
    }

    public void setChannelPublishSuccess(boolean channelPublishSuccess) {
        this.channelPublishSuccess = channelPublishSuccess;
    }

    private boolean channelPublishSuccess;
}
