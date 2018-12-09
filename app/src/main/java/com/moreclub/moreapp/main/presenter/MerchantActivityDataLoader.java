package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-02.
 */

public class MerchantActivityDataLoader extends BasePresenter<IMerchantActivityDataLoader.LoadMerchantActivityDataBinder> implements IMerchantActivityDataLoader {
    @Override
    public void onLoadMerchantActivity(Integer mid,Long uid,Integer pn,Integer ps) {
        Callback<RespDto<List<MerchantActivity>>> callback = new Callback<RespDto<List<MerchantActivity>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantActivity>>> call, Response<RespDto<List<MerchantActivity>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMerchantActivityResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMerchantActivityFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMerchantActivityFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantActivity>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantActivityFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadMerchantActivity(mid,uid,pn,ps).enqueue(callback);
    }
}
