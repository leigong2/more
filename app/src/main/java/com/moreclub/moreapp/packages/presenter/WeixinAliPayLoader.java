package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.packages.model.OrderId;
import com.moreclub.moreapp.packages.model.OrderStatus;
import com.moreclub.moreapp.packages.model.PayParam;
import com.moreclub.moreapp.packages.model.AppPayResult;
import com.moreclub.moreapp.packages.model.ZeroPayParam;
import com.moreclub.moreapp.ucenter.model.OrderRepayValidateResult;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/28.
 */

public class WeixinAliPayLoader extends BasePresenter<IWeixinAliPayLoader.LoaderWeixinPayDataBinder> implements IWeixinAliPayLoader {
    @Override
    public void weixinPay(String payTag, PayParam param) {
        Callback callback = new Callback<RespDto<AppPayResult>>() {
            @Override
            public void onResponse(Call<RespDto<AppPayResult>> call,
                                   Response<RespDto<AppPayResult>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onWeixinPayFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onWeixinPayResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<AppPayResult>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onWeixinPayFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onWeixinPay(payTag,param).enqueue(callback);

    }

    @Override
    public void aliPay(String payTag, PayParam param) {

        Callback callback = new Callback<RespDto<AppPayResult>>() {
            @Override
            public void onResponse(Call<RespDto<AppPayResult>> call,
                                   Response<RespDto<AppPayResult>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onAliPayFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onAliPayResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<AppPayResult>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onAliPayFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onAliPay(payTag,param).enqueue(callback);

    }

    @Override
    public void notifyServerWeixinSuccess(String orderid) {

        Callback callback = new Callback<RespDto<OrderStatus>>() {
            @Override
            public void onResponse(Call<RespDto<OrderStatus>> call,
                                   Response<RespDto<OrderStatus>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onNotifyServerWeixinFailure("401");
                    return;
                }

                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onNotifyServerWeixinSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<OrderStatus>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onNotifyServerWeixinFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onNotifyServerWeixinPay(orderid).enqueue(callback);

    }

    @Override
    public void notifyServerAliSuccess(String orderid) {

        Callback callback = new Callback<RespDto<OrderStatus>>() {
            @Override
            public void onResponse(Call<RespDto<OrderStatus>> call,
                                   Response<RespDto<OrderStatus>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onNotifyServerAliFailure("401");
                    return;
                }

                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onNotifyServerAliSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<OrderStatus>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onNotifyServerAliFailure(t.getMessage());
            }
        };
//        OrderId orderIDObject = new OrderId();
//        orderIDObject.setOrderId(orderid);
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onNotifyServerAliPay(orderid).enqueue(callback);

    }

    @Override
    public void loadOrderStatus(String orderid) {
        Callback callback = new Callback<RespDto<OrderStatus>>() {
            @Override
            public void onResponse(Call<RespDto<OrderStatus>> call,
                                   Response<RespDto<OrderStatus>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onOrderStatusFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onOrderStatusReponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<OrderStatus>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onOrderStatusFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoaderOrderStatus(orderid).enqueue(callback);
    }

    @Override
    public void orderExpire(String orderid) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onOrderExpireFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onOrderExpireResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onOrderExpireFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onOrderExpire(orderid).enqueue(callback);
    }

    @Override
    public void zeroPay(ZeroPayParam param) {

        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onZeroPayFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onZeroPayResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onZeroPayFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onZeroPay(param).enqueue(callback);

    }

    @Override
    public void onRepayValidate(RepayValidateParam param) {

        Callback callback = new Callback<RespDto<OrderRepayValidateResult>>() {
            @Override
            public void onResponse(Call<RespDto<OrderRepayValidateResult>> call,
                                   Response<RespDto<OrderRepayValidateResult>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onRepayValidateFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onRepayValidateResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onRepayValidateFailure("C10110");
                }
            }

            @Override
            public void onFailure(Call<RespDto<OrderRepayValidateResult>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onRepayValidateFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        com.moreclub.moreapp.ucenter.api.ApiInterface.ApiFactory.createApi(token).onRepayValidate(param).enqueue(callback);

    }
}
