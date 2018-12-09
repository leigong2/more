package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/2/23.
 */

public class BizAreaBarsDataLoader extends BasePresenter<IBizAreaBarsDataLoader.LoadBarsDataBinder>
        implements IBizAreaBarsDataLoader {
    @Override
    public void onLoadMerchants(int bid, int page, int size) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess() && getBinder() != null) {
                    getBinder().onMerchantsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onMerchantsFailure(t.getMessage());
                }
            }
        };

        if (MoreUser.getInstance().getUid()!=null&&MoreUser.getInstance().getUid()>0){
            ApiInterface.ApiFactory.createApi().onLoadBizAreaMerchants(bid, page, size,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadBizAreaMerchantsNoUid(bid, page, size).enqueue(callback);
        }

    }

    @Override
    public void onLoadSeachMerchants(String keyword, String city, int page, int size) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess() && getBinder() != null) {
                    getBinder().onMerchantsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onMerchantsFailure(t.getMessage());
                }
            }
        };
        if (MoreUser.getInstance().getUid()!=null&&MoreUser.getInstance().getUid()>0){
            ApiInterface.ApiFactory.createApi().onLoadSearchMerchants(keyword,
                    city, page, size,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadSearchMerchantsNoUid(keyword,
                    city, page, size).enqueue(callback);
        }

    }

    @Override
    public void loadNearbyMerchant(String city, Integer pn, Integer ps, String location) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess()) {
                    if (getBinder() != null)
                        getBinder().onNearbyMerchantsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onNearbyMerchantsFailure(t.getMessage());
            }
        };
        if (MoreUser.getInstance().getUid()!=null&&MoreUser.getInstance().getUid()>0){
            ApiInterface.ApiFactory.createApi().onLoadNearbyMerchants(city, pn, ps,city, location,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadNearbyMerchantsNoUid(city, pn, ps,city, location).enqueue(callback);
        }

    }
}


