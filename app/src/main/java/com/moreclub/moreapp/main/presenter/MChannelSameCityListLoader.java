package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/7/28.
 */

public class MChannelSameCityListLoader extends BasePresenter<IMChannelSameCityListLoader.LoadMChannelSameCityListDataBinder> implements IMChannelSameCityListLoader {

    @Override
    public void loadMChannelSameCity(long uid, int city, int page, int pageSize) {
        Callback<RespDto<ArrayList<MainChannelItem>>> callback = new Callback<RespDto<ArrayList<MainChannelItem>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MainChannelItem>>> call, Response<RespDto<ArrayList<MainChannelItem>>> response) {
                if (getBinder() != null) {
                    if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                        getBinder().onMChannelSameCityResponse(response.body());
                    } else {
                        if (getBinder() != null && response != null && response.body() != null) {
                            getBinder().onMChannelSameCityFailure(response.body().getErrorDesc());
                        } else {
                            getBinder().onMChannelSameCityFailure(null);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MainChannelItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMChannelSameCityFailure(t.getMessage());
            }
        };
        com.moreclub.moreapp.information.api.ApiInterface.ApiFactory.createApi().
                onLoadChannelSameCityList(uid,city, page, pageSize).enqueue(callback);


    }
}
