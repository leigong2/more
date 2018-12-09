package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-05-12.
 */

public class UserBindPhone {
    private static UserBindPhone userBindPhone = new UserBindPhone();
    /**
     * appVersion : string
     * phone : string
     * sign : string
     * timestamp : 0
     * uid : 0
     * vcode : string
     */

    private String appVersion;
    private String phone;
    private String sign;
    private long timestamp;
    private long uid;
    private String vcode;

    public static UserBindPhone getInstance() {
        return userBindPhone;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getVcode() {
        return vcode;
    }

    public void setVcode(String vcode) {
        this.vcode = vcode;
    }

    @Override
    public String toString() {
        return "UserBindPhone{" +
                "appVersion='" + appVersion + '\'' +
                ", phone='" + phone + '\'' +
                ", sign='" + sign + '\'' +
                ", timestamp=" + timestamp +
                ", uid=" + uid +
                ", vcode='" + vcode + '\'' +
                '}';
    }
}
