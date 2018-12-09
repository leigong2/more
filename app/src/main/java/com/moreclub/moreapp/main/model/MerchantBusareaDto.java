package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class MerchantBusareaDto {
    private Integer busId;
    private int merchantNum;
    private String busName;
    private List<MerchantHomeDto> merchants;
    private int hot;

    public Integer getBusId() {
        return busId;
    }

    public void setBusId(Integer busId) {
        this.busId = busId;
    }

    public int getMerchantNum() {
        return merchantNum;
    }

    public void setMerchantNum(int merchantNum) {
        this.merchantNum = merchantNum;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public List<MerchantHomeDto> getMerchants() {
        return merchants;
    }

    public void setMerchants(List<MerchantHomeDto> merchants) {
        this.merchants = merchants;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }
}
