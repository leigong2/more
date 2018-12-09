package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-05-22.
 */

public class UserCheckAnswer {

    /**
     * answer : string
     * appVersion : string
     * appkey : string
     * phone : string
     * sign : string
     * timestamp : 0
     * uid : 0
     */

    private String answer;
    private String appVersion;
    private String appkey;
    private String phone;
    private String sign;
    private int timestamp;
    private int uid;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
