package com.moreclub.moreapp.main.model;

import java.util.List;

/**
 * Created by Administrator on 2017/5/26.
 */

public class HomeRecomMerchants {
    private List<MerchantBusareaDto> respList;

    private int serverTime;

    private HomeSkyMap homePageSkyMap;

    public List<MerchantBusareaDto> getRespList() {
        return respList;
    }

    public void setRespList(List<MerchantBusareaDto> respList) {
        this.respList = respList;
    }

    public int getServerTime() {
        return serverTime;
    }

    public void setServerTime(int serverTime) {
        this.serverTime = serverTime;
    }

    public HomeSkyMap getHomePageSkyMap() {
        return homePageSkyMap;
    }

    public void setHomePageSkyMap(HomeSkyMap homePageSkyMap) {
        this.homePageSkyMap = homePageSkyMap;
    }
}
