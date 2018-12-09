package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/2/23.
 */

public class TotalBarsDataLoader extends BasePresenter<ITotalBarsDataLoader.LoadTotalBarsDataBinder>
        implements ITotalBarsDataLoader {
    @Override
    public void onLoadMerchants(String city, int page, int size) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess() && getBinder() != null){
                    if (getBinder() != null) {
                        getBinder().onMerchantsResponse(response.body());
                    }
                } else {
                    if (getBinder() != null) {
                        getBinder().onMerchantsFailure(response.body().getErrorDesc());
                    }
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
            ApiInterface.ApiFactory.createApi().onLoadMainMerchants(city, page, size,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadMainMerchantsNoUid(city, page, size).enqueue(callback);
        }

    }

    @Override
    public void onLoadSeachMerchants(String keyword, String city, int page, int size) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess() && getBinder() != null){
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
    public void onLoadCouponMerchants(String city, int model,
                                      int page, int size, String location) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess() && getBinder() != null){
                    getBinder().onCouponMerchantsResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onCouponMerchantFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadCouponMerchants(city,
                model, page, size,location).enqueue(callback);
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


