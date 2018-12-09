package com.moreclub.moreapp.main.model;

/**
 * Created by Captain on 2017/4/14.
 */

public class MerchantCardPromoExtra {

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String remark;
    private String title;

    public String getWineName() {
        return wineName;
    }

    public void setWineName(String wineName) {
        this.wineName = wineName;
    }

    public int getWineCount() {
        return wineCount;
    }

    public void setWineCount(int wineCount) {
        this.wineCount = wineCount;
    }

    public String getExWineCollect() {
        return exWineCollect;
    }

    public void setExWineCollect(String exWineCollect) {
        this.exWineCollect = exWineCollect;
    }

    private String wineName;
    private int wineCount;
    private String exWineCollect;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTipStr() {
        return tipStr;
    }

    public void setTipStr(String tipStr) {
        this.tipStr = tipStr;
    }

    private int type;
    private String tipStr;
}
