package com.moreclub.moreapp.packages.model;

/**
 * Created by Captain on 2017/3/24.
 */

public class PackageDetailsSubContent {

//                        content = "\U4efd\U996d,\U5e7f\U544a";
//        option = "2\U90092";
//        pid = 3;
//        productType = 2;
//        typeRemark = "\U6d0b\U9152";

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getTypeRemark() {
        return typeRemark;
    }

    public void setTypeRemark(String typeRemark) {
        this.typeRemark = typeRemark;
    }

    private String content;
    private String option;
    private int pid;
    private int productType;
    private String typeRemark;
}
