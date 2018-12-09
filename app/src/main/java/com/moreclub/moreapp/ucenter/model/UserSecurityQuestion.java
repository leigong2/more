package com.moreclub.moreapp.ucenter.model;

import java.util.List;

/**
 * Created by Administrator on 2017-05-22.
 */

public class UserSecurityQuestion {

    private DataBean data;
    private int elapsedMilliseconds;
    private String errorCode;
    private String errorDesc;
    private boolean success;
    public static class DataBean{
        private String ext;
        private int id;
        private String name;
        private List<DicsBean> dics;

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<DicsBean> getDics() {
            return dics;
        }

        public void setDics(List<DicsBean> dics) {
            this.dics = dics;
        }

        public static class DicsBean {
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "ext='" + ext + '\'' +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", dics=" + dics +
                    '}';
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

    @Override
    public String toString() {
        return "UserSecurityQuestion{" +
                "data=" + data +
                ", elapsedMilliseconds=" + elapsedMilliseconds +
                ", errorCode='" + errorCode + '\'' +
                ", errorDesc='" + errorDesc + '\'' +
                ", success=" + success +
                '}';
    }
}
