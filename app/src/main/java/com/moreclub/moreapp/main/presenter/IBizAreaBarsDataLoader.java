package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.List;

/**
 * 加载商圈数据的逻辑
 */

@Implement(BizAreaBarsDataLoader.class)
public interface IBizAreaBarsDataLoader<T> {

    void onLoadMerchants(int bid, int page, int size);

    void onLoadSeachMerchants(String keyword, String city, int page, int size);

    void loadNearbyMerchant(String city, Integer pn, Integer ps, String location);

    interface LoadBarsDataBinder<T> extends DataBinder {
        void onMerchantsResponse(RespDto<List<MerchantItem>> response);

        void onMerchantsFailure(String msg);

        void onNearbyMerchantsResponse(RespDto<List<MerchantItem>> response);

        void onNearbyMerchantsFailure(String msg);
    }
}