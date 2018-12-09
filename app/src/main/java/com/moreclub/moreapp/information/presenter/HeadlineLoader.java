package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.Headline;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-16.
 */

public class HeadlineLoader extends BasePresenter<IHeadlineLoader.LoadHeadlineBinder> implements IHeadlineLoader {
    @Override
    public void onLoadHeadlineList(Integer cityId, Integer pageIndex, Integer pageSize) {
        Callback<RespDto<Headline>> callback = new Callback<RespDto<Headline>>() {
            @Override
            public void onResponse(Call<RespDto<Headline>> call, Response<RespDto<Headline>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onGetHeadlineResponse(response.body());
                } else {
                    if (getBinder() != null &&response != null && response.body() != null) {
                        getBinder().onGetHeadlineFailure(response.body().getErrorDesc());
                    } else if (getBinder() != null){
                        getBinder().onGetHeadlineFailure(null);
                    }
                }
            }
            @Override
            public void onFailure(Call<RespDto<Headline>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetHeadlineFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadHeadlineList(cityId, pageIndex, pageSize).enqueue(callback);
    }
}
