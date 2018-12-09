package com.moreclub.moreapp.message.model;

/**
 * Created by Captain on 2017/5/4.
 */

public class MessageSystem {
//    {
//        "sid":1527,
//            "title":"aaaaa",
//            "content":"qeqweqwe",
//            "toUser":3097832504509440, "sendTime":1493348249000, "type":"system", "template":
//        "index", "pushId":"-1", "extras":null, "platform":"all", "map":{
//        "linktext":""
//    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getToUser() {
        return toUser;
    }

    public void setToUser(long toUser) {
        this.toUser = toUser;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public MessageSystemExtras getMap() {
        return map;
    }

    public void setMap(MessageSystemExtras map) {
        this.map = map;
    }

    private int sid;
    private String title;
    private String content;
    private long toUser;
    private long sendTime;
    private String type;
    private String template;
    private String pushId;
    private String platform;
    private MessageSystemExtras map;



}
