package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-20.
 */

public class AddSignInterLoader extends BasePresenter<IAddSignInterLoader.AddSignInterDataBinder> implements IAddSignInterLoader {

    @Override
    public void onAddSignInter(String token, AddSignInter inter) {
        Callback<RespDto<MerchantsSignInters>> callback = new Callback<RespDto<MerchantsSignInters>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantsSignInters>> call, Response<RespDto<MerchantsSignInters>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onAddSignResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<MerchantsSignInters>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onAddSignFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onAddSignInter(inter).enqueue(callback);
    }

    @Override
    public void loadNearbyMerchant(String city, Integer pn, Integer ps, String location) {

        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess()){
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
            ApiInterface.ApiFactory.createApi().onLoadNearbyMerchants(city, pn, ps,city,location,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadNearbyMerchantsNoUid(city, pn, ps,city,location).enqueue(callback);
        }

    }
}
