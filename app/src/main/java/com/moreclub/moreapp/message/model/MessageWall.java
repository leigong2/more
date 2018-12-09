package com.moreclub.moreapp.message.model;

/**
 * Created by Administrator on 2017-06-07.
 */

public class MessageWall {

    /**
     * content : 满井余孽 回复 我:回复 王志龙 : 测试
     * map : {"commentId":"445","content":"回复 王志龙 : 测试","fromUserName":"满井余孽","isMyAct":"false","parentId":"441"}
     * platform : all
     * pushId : 181
     * sendTime : 1496807373000
     * sid : 2138
     * template : activity
     * title : 为这座城的爵士乐守望
     * toUser : 993870247556
     * type : wall
     */

    private String content;
    private MapBean map;
    private String platform;
    private String pushId;
    private long sendTime;
    private int sid;
    private String template;
    private String title;
    private long toUser;
    private String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getToUser() {
        return toUser;
    }

    public void setToUser(long toUser) {
        this.toUser = toUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class MapBean {
        /**
         * commentId : 445
         * content : 回复 王志龙 : 测试
         * fromUserName : 满井余孽
         * isMyAct : false
         * parentId : 441
         */

        private String commentId;
        private String content;
        private String fromUserName;
        private String isMyAct;
        private String parentId;

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getIsMyAct() {
            return isMyAct;
        }

        public void setIsMyAct(String isMyAct) {
            this.isMyAct = isMyAct;
        }

        public String getParentId() {
            return parentId;
        }

        public void setParentId(String parentId) {
            this.parentId = parentId;
        }
    }
}
