package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.CorrectSignin;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Topic;

import java.util.List;

/**
 * Created by Captain on 2017/5/25.
 */
@Implement(NearbyMerchantLoader.class)
public interface INearbyMerchantLoader<T> {


    void loadNearbyMerchant(String city, Integer pn, Integer ps, String location);

    void onCorrectSign(CorrectSignin param);

    void loadTopicList(int cityid);

    interface LoaderNearbyMerchantDataBinder<T> extends DataBinder{

        void onNearbyMerchantsResponse(RespDto<List<MerchantItem>> response);

        void onNearbyMerchantsFailure(String msg);

        void onCorrectSignResponse(RespDto<Boolean> response);
        void onCorrectSignFailure(String msg);

        void onTopicListResponse(RespDto<List<Topic>> response);
        void onTopicListFailure(String msg);

    }
}
