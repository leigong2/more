package com.moreclub.moreapp.chat.model;

/**
 * Created by Captain on 2017/6/9.
 */

public class ChatGroupUser {
//    {"uid":50472913028,"thumb":"http://more-user.oss-cn-beijing.aliyuncs.com/2016/12/02/u_50472913028_1480670014.jpg",
// "nickname":"咯咯咯咯咯","sex":"1","birthday":"1989-01-05"}

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    private long uid;
    private String thumb;
    private String nickname;
    private String sex;
    private String birthday;

}
