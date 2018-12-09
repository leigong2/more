package com.moreclub.moreapp.packages.model;

/**
 * Created by Captain on 2017/3/28.
 */

public class AppPayResult {
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(long partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
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

    public String getWx_package() {
        return wx_package;
    }

    public void setWx_package(String wx_package) {
        this.wx_package = wx_package;
    }

    private String appid;
    private String noncestr;
    private long orderId;
    private long partnerid;
    private String prepayid;
    private String sign;
    private long timestamp;
    private String wx_package;

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getEncodeParam() {
        return encodeParam;
    }

    public void setEncodeParam(String encodeParam) {
        this.encodeParam = encodeParam;
    }

    private String param;
    private String encodeParam;

//    appid = wx40f8c60fb6dc7a71;
//    noncestr = gH2c8LnZsDwvqUIMyKsePz23e2tbzheE;
//    orderId = 90870684811897;
//    partnerid = 1408333302;
//    prepayid = wx20170328161425e344dc27240061355255;
//    sign = 001EE9140A1E20E05CBFA36B043046D8;
//    timestamp = 1490688865;
//    "wx_package" = "Sign=WXPay";

}
