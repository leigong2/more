package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantCard;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/4/14.
 */

public class MerchantCardPromotionLoader extends BasePresenter<IMerchantCardPromotionLoader.LoadMerchantCardPromotion> implements IMerchantCardPromotionLoader {
    @Override
    public void loadMerchantCardPromotion(String mid, int cardType) {

        Callback<RespDto<MerchantCard>> callback = new Callback<RespDto<MerchantCard>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantCard>> call, Response<RespDto<MerchantCard>> response) {

                if (getBinder() != null && response.body() != null && response.body().isSuccess())
                    getBinder().onMerchantCardPromotionResponse(response.body());

            }

            @Override
            public void onFailure(Call<RespDto<MerchantCard>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantCardPromotionFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onMerchantCardPromotion(mid, cardType).enqueue(callback);

    }
}
