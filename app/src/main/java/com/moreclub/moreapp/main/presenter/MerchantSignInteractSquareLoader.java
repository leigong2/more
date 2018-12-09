package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.SignSpaceList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/2.
 */

public class MerchantSignInteractSquareLoader extends BasePresenter<IMerchantSignInteractSquareLoader.LoadMerchantSignInteractSquareDataBinder> implements IMerchantSignInteractSquareLoader {


    @Override
    public void onLoadSignList(String mid, String uid, String pn, String ps) {

        Callback<RespDto<SignSpaceList>> callback = new Callback<RespDto<SignSpaceList>>() {
            @Override
            public void onResponse(Call<RespDto<SignSpaceList>> call, Response<RespDto<SignSpaceList>> response) {

                if (response != null && response.code() == 401) {
                    if (getBinder() != null)
                        getBinder().onSignListFailure("401");
                    return;
                }
                if (response != null && response.body() != null && response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onSignListResponse(response.body());
                }

            }

            @Override
            public void onFailure(Call<RespDto<SignSpaceList>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSignListFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onSignMoreList(mid, mid, uid, pn, ps).enqueue(callback);
    }

}
