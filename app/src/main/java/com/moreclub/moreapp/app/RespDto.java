package com.moreclub.moreapp.app;

/**
 * Created by Administrator on 2017/2/23.
 */

public class RespDto<T> {
    private boolean success;

    /**
     * 返回的数据
     */
    private T data;

    public T getList() {
        return list;
    }

    public void setList(T list) {
        this.list = list;
    }

    private T list;

    /**
     * 错误代码
     */
    private String errorCode;

    /**
     * 错误描述
     */
    private String errorDesc;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "RespDto{" +
                "success=" + success +
                ", data=" + data +
                ", errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                '}';
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

