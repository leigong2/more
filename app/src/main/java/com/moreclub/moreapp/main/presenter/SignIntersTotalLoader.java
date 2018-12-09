package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantsSignInters;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-12-0012.
 */

public class SignIntersTotalLoader extends BasePresenter<ISignIntersTotalLoader.LoadSignIntersTotalDataBinder>
        implements ISignIntersTotalLoader {
    @Override
    public void onLoadSignIntersTotal(String city, Integer pn) {
        Callback<RespDto<List<MerchantsSignInters>>> callback = new Callback<RespDto<List<MerchantsSignInters>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantsSignInters>>> call, Response<RespDto<List<MerchantsSignInters>>> response) {
                if (getBinder() == null) return;
                if (response != null && response.body() != null) {
                    getBinder().onSignIntersTotalResponse(response.body());
                } else {
                    getBinder().onSignIntersTotalFailure("");
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantsSignInters>>> call, Throwable t) {

            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSignIntersTotal(city, pn).enqueue(callback);
    }
}
