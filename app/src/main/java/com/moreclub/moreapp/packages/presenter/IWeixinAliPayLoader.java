package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.OrderStatus;
import com.moreclub.moreapp.packages.model.PayParam;
import com.moreclub.moreapp.packages.model.AppPayResult;
import com.moreclub.moreapp.packages.model.ZeroPayParam;
import com.moreclub.moreapp.ucenter.model.OrderRepayValidateResult;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;

/**
 * Created by Captain on 2017/3/28.
 */
@Implement(WeixinAliPayLoader.class)
public interface IWeixinAliPayLoader<T> {

    void weixinPay(String payTag,PayParam param);

    void aliPay(String payTag,PayParam param);

    void notifyServerWeixinSuccess(String orderid);

    void notifyServerAliSuccess(String orderid);

    void loadOrderStatus(String orderid);

    void orderExpire(String orderid);

    void zeroPay(ZeroPayParam param);

    void onRepayValidate(RepayValidateParam param);

    interface LoaderWeixinPayDataBinder extends DataBinder{

        void onWeixinPayResponse(RespDto<AppPayResult> response);

        void onWeixinPayFailure(String msg);

        void onAliPayResponse(RespDto<AppPayResult> response);

        void onAliPayFailure(String msg);

        void onNotifyServerWeixinSuccess(RespDto<OrderStatus> response);

        void onNotifyServerWeixinFailure(String msg);

        void onNotifyServerAliSuccess(RespDto<OrderStatus> response);

        void onNotifyServerAliFailure(String msg);

        void onOrderStatusReponse(RespDto<OrderStatus> response);

        void onOrderStatusFailure(String msg);

        void onOrderExpireResponse(RespDto<String> response);

        void onOrderExpireFailure(String msg);

        void onZeroPayResponse(RespDto<Boolean> response);
        void onZeroPayFailure(String msg);

        void onRepayValidateResponse(RespDto<OrderRepayValidateResult> response);
        void onRepayValidateFailure(String msg);
    }
}
