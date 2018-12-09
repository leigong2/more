package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantsSignInters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-06-21.
 */

public class SignInterDetailLoader extends BasePresenter<ISignInterDetailLoader.LoadSignInterDetailBinder> implements ISignInterDetailLoader {

    @Override
    public void endSignInter(String token, Integer sid) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (response.body().isSuccess())
                    if (getBinder() != null) {
                        getBinder().onEndResponse(response.body());
                    }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onEndFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi(token).onEndSignInterDetail(sid).enqueue(callback);
    }

    @Override
    public void onLoadSignInterDetail(String token, Integer sid) {
        Callback<RespDto<MerchantsSignInters>> callback = new Callback<RespDto<MerchantsSignInters>>() {
            @Override
            public void onResponse(Call<RespDto<MerchantsSignInters>> call, Response<RespDto<MerchantsSignInters>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onLoadInterDetailResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<MerchantsSignInters>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onLoadInterDetailFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi(token).onLoadSignInterDetail(sid).enqueue(callback);
    }

    @Override
    public void onDeleteSignInterDetail(String token, Integer sid) {
        Callback<RespDto<Object>> callback = new Callback<RespDto<Object>>() {
            @Override
            public void onResponse(Call<RespDto<Object>> call, Response<RespDto<Object>> response) {
                if (response.body().isSuccess())
                    if (getBinder() != null) {
                        getBinder().onDeleteInterDetailResponse(response.body());
                    }
            }

            @Override
            public void onFailure(Call<RespDto<Object>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onDeleteInterDetailFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi(token).onDeleteSignInterDetail(sid).enqueue(callback);
    }

    @Override
    public void onPostSignInter(String token, SignInter inter) {
        Callback<RespDto<Boolean>> callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call, Response<RespDto<Boolean>> response) {
                if (getBinder() != null && response != null && response.body() != null)
                    getBinder().onPostSignInterResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPostSignInterFailure(t.getMessage());
            }

        };
        ApiInterface.ApiFactory.createApi(token).onPostSignInter(inter).enqueue(callback);
    }
}