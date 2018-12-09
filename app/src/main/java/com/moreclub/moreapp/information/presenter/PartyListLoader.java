package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.api.ApiInterface;
import com.moreclub.moreapp.information.model.ActivityData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-18.
 */

public class PartyListLoader extends BasePresenter<IPartyListLoader.LoadPartyListBinder> implements IPartyListLoader {

    @Override
    public void onLoadPartyList(String cityCode, Integer pn, Integer ps, Integer type, Long uid) {
        Callback<RespDto<ActivityData>> callback = new Callback<RespDto<ActivityData>>() {
            @Override
            public void onResponse(Call<RespDto<ActivityData>> call, Response<RespDto<ActivityData>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onGetPartyListResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onGetPartyListFaiure(response.body().getErrorDesc());
                    } else {
                        if (getBinder() != null)
                            getBinder().onGetPartyListFaiure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<ActivityData>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetPartyListFaiure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadActivitiesList(cityCode, pn, ps, type, uid).enqueue(callback);
    }
}
