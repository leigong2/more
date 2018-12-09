package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.NewStore;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-16.
 */

public class NewStoreLoader extends BasePresenter<INewStoreLoader.LoadNewStoreBinder> implements INewStoreLoader {
    @Override
    public void onLoadNewStoreList(String cityCode, Integer pageIndex, Integer pageSize) {
        Callback<RespDto<NewStore>> callback = new Callback<RespDto<NewStore>>() {
            @Override
            public void onResponse(Call<RespDto<NewStore>> call, Response<RespDto<NewStore>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onGetNewStoreResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onGetNewStoreFailure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onGetNewStoreFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<NewStore>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetNewStoreFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadNewStoreList(cityCode, pageIndex, pageSize).enqueue(callback);
    }
}
