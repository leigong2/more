package com.moreclub.moreapp.ucenter.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.DetailOrder;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/27.
 */

public class OrderDetailLoader extends BasePresenter<IOrderDetailLoader.LoaderOrderDetailDataBinder>
        implements IOrderDetailLoader {
    @Override
    public void getPkgOrderDetail(long orderId, long uid) {
        Callback callback = new Callback<RespDto<DetailOrder>>() {
            @Override
            public void onResponse(Call<RespDto<DetailOrder>> call,
                                   Response<RespDto<DetailOrder>> response) {
                if (getBinder() != null)
                    getBinder().onOrderResponse(response);
            }

            @Override
            public void onFailure(Call<RespDto<DetailOrder>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onOrderFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadOrderDetail(orderId, uid).enqueue(callback);
    }
}
