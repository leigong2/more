package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.SignSpaceList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-07-26-0026.
 */

public class SignInterTotalLoader extends BasePresenter<ISignInterTotalLoader.LoadSignInterTotalDataBinder>
        implements ISignInterTotalLoader {
    @Override
    public void onLoadSignInterTotal(String city,Integer pn,Integer ps) {
        Callback<RespDto<SignSpaceList>> callback = new Callback<RespDto<SignSpaceList>>() {
            @Override
            public void onResponse(Call<RespDto<SignSpaceList>> call, Response<RespDto<SignSpaceList>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onSignInterTotalResponse(response.body());
                else if (getBinder() != null)
                    getBinder().onSignInterTotalFailure("");
            }

            @Override
            public void onFailure(Call<RespDto<SignSpaceList>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onSignInterTotalFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSignInterTotal(city,pn,ps).enqueue(callback);
    }
}
