package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MerchantsSignInters;

import java.util.List;

/**
 * Created by Administrator on 2017-06-20.
 */

@Implement(AddSignInterLoader.class)
public interface IAddSignInterLoader {
    void onAddSignInter(String token, AddSignInter inter);
    void loadNearbyMerchant(String city, Integer pn, Integer ps, String location);

    interface AddSignInterDataBinder<T> extends DataBinder {
        void onAddSignResponse(RespDto<MerchantsSignInters> response);
        void onAddSignFailure(String msg);

        void onNearbyMerchantsResponse(RespDto<List<MerchantItem>> response);
        void onNearbyMerchantsFailure(String msg);
    }
}
