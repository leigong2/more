package com.moreclub.moreapp.message.model;

/**
 * Created by Captain on 2017/5/4.
 */

public class MessageSystemExtras {

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getLinktext() {
        return linktext;
    }

    public void setLinktext(String linktext) {
        this.linktext = linktext;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    private String src;
    private String linktext;
    private String pushTitle;

}
