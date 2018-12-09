package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantsSignInters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/2.
 */

public class MerchantInteractLoader extends BasePresenter<IMerchantInteractLoader.LoadMerchantInteractDataBinder> implements IMerchantInteractLoader {

    @Override
    public void onLoadOpenMerchantInters(String token, Integer mid, Integer pn) {
        Callback<List<MerchantsSignInters>> callback = new Callback<List<MerchantsSignInters>>() {
            @Override
            public void onResponse(Call<List<MerchantsSignInters>> call, Response<List<MerchantsSignInters>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onOpenMerchantIntersResponse(response);
                else if (getBinder() != null)
                    getBinder().onOpenMerchantIntersFailure("");

            }

            @Override
            public void onFailure(Call<List<MerchantsSignInters>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onOpenMerchantIntersFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onLoadMerchantInters(mid, pn).enqueue(callback);
    }
}
