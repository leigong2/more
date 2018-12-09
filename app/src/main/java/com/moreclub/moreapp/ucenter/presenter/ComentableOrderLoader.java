package com.moreclub.moreapp.ucenter.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.OrderResp;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;
import com.moreclub.moreapp.ucenter.model.UserOrderDelParam;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/27.
 */

public class ComentableOrderLoader extends BasePresenter<IComentableOrderLoader.LoaderOrderDataBinder>
        implements IMyOrderLoader {
    @Override
    public void getAllOrderList(String type, long uid, int pageNum, int pageSize) {
        Callback callback = new Callback<RespDto<OrderResp>>() {
            @Override
            public void onResponse(Call<RespDto<OrderResp>> call,
                                   Response<RespDto<OrderResp>> response) {
                if (getBinder() != null)
                    getBinder().onOrderResponse(response);
            }

            @Override
            public void onFailure(Call<RespDto<OrderResp>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onOrderFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadOrder(type, uid, pageNum, pageSize).enqueue(callback);
    }

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

    }

    @Override
    public void onRepayValidate(RepayValidateParam param) {

    }
}