package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.CorrectSignin;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/25.
 */

public class NearbyMerchantLoader extends BasePresenter<INearbyMerchantLoader.LoaderNearbyMerchantDataBinder> implements INearbyMerchantLoader{

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

    @Override
    public void onCorrectSign(CorrectSignin param) {
        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onCorrectSignFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onCorrectSignResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onCorrectSignFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onCorrectSignin(param).enqueue(callback);
    }

    @Override
    public void loadTopicList(int cityid) {

        Callback callback = new Callback<RespDto<List<Topic>>>() {
            @Override
            public void onResponse(Call<RespDto<List<Topic>>> call,
                                   Response<RespDto<List<Topic>>> response) {
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onTopicListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<Topic>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onTopicListFailure(t.getMessage());
            }
        };

        ApiInterface.ApiFactory.createApi().onLoadtopics(cityid).enqueue(callback);
    }
}
