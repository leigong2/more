package com.moreclub.moreapp.ucenter.model;

/**
 * Created by Administrator on 2017-05-22.
 */

public class UserCheckAnswerResp {

    /**
     * data : string
     * elapsedMilliseconds : 0
     * errorCode : string
     * errorDesc : string
     * success : true
     */

    private String data;
    private int elapsedMilliseconds;
    private String errorCode;
    private String errorDesc;
    private boolean success;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getElapsedMilliseconds() {
        return elapsedMilliseconds;
    }

    public void setElapsedMilliseconds(int elapsedMilliseconds) {
        this.elapsedMilliseconds = elapsedMilliseconds;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
