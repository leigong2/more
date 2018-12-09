package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.CollectParam;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.DictionaryName;
import com.moreclub.moreapp.packages.model.PkgRefundReq;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/31.
 */

public class RefundReasonDataLoader extends BasePresenter<IRefundResonDataLoader.
        LoaderReasonDataBinder> implements IRefundResonDataLoader {
    @Override
    public void getRefundReson() {
        Callback callback = new Callback<RespDto<DictionaryName>>() {
            @Override
            public void onResponse(Call<RespDto<DictionaryName>> call,
                                   Response<RespDto<DictionaryName>> response) {

                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<DictionaryName>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadRefundReason().enqueue(callback);
    }

    @Override
    public void doPkgRefund(PkgRefundReq req) {
        Callback callback = new Callback<RespDto<Long>>() {
            @Override
            public void onResponse(Call<RespDto<Long>> call,
                                   Response<RespDto<Long>> response) {

                if (getBinder() != null)
                    getBinder().onPkgRefundResponse(response);

            }

            @Override
            public void onFailure(Call<RespDto<Long>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onPkgRefundFailure(t.getMessage());
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onPkgOrderRefund(req).enqueue(callback);
    }
}
