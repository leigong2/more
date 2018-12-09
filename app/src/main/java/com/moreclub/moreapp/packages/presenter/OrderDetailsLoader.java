package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;
import com.moreclub.moreapp.ucenter.model.UserOrderDelParam;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/8/14.
 */

public class OrderDetailsLoader extends BasePresenter<IOrderDetailsLoader
        .LoaderOrderDetailsDataBinder> implements IOrderDetailsLoader  {
    @Override
    public void delOrder(String orderID) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onDelOrderFailure("401");
                    return;
                }
                if (getBinder() != null)
                    getBinder().onDelOrderResponse(response.body());
            }
            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onDelOrderFailure(t.getMessage());
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        UserOrderDelParam delParam = new UserOrderDelParam();
        delParam.setUid(""+MoreUser.getInstance().getUid());
        ApiInterface.ApiFactory.createApi(token).onOrderDel(orderID,delParam).enqueue(callback);
    }

    @Override
    public void cancelOrder(UserOrderCancelParam param) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onCancelOrderFailure("401");
                    return;
                }
                if (getBinder() != null)
                    getBinder().onCancelOrderResponse(response.body());
            }
            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onCancelOrderFailure(t.getMessage());
            }
        };

        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onOrderCancel(param).enqueue(callback);
    }
}
