package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-08-26-0026.
 */

public class UserPartyListLoader extends BasePresenter<IUserPartyListLoader.LoadUserPartyListBinder> implements IUserPartyListLoader {

    @Override
    public void onLoadPartyList(Integer mid, Long uid, Integer pn, Integer ps) {
        Callback<RespDto<List<MerchantActivity>>> callback = new Callback<RespDto<List<MerchantActivity>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantActivity>>> call, Response<RespDto<List<MerchantActivity>>> response) {
                if (getBinder() != null) {
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
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantActivity>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onGetPartyListFaiure(t.getMessage());
            }
        };
        com.moreclub.moreapp.main.api.ApiInterface.ApiFactory.createApi().onLoadMerchantActivity(mid, uid, pn, ps).enqueue(callback);
    }
}
