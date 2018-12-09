package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.OrderRepayValidateResult;
import com.moreclub.moreapp.ucenter.model.OrderResp;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;

import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/17.
 */

@Implement(MyOrderLoader.class)
public interface IMyOrderLoader<T> {
    void getAllOrderList(String type, long uid, int pageNum, int pageSize);
    void delOrder(String orderID);
    void cancelOrder(UserOrderCancelParam param);
    void onRepayValidate(RepayValidateParam param);
    interface LoaderOrderDataBinder<T> extends DataBinder {
        void onOrderResponse(Response<RespDto<OrderResp>> response);
        void onOrderFailure(String msg);

        void onDelOrderResponse(RespDto<String> response);
        void onDelOrderFailure(String msg);

        void onCancelOrderResponse(RespDto<String> response);
        void onCancelOrderFailure(String msg);

        void onRepayValidateResponse(RespDto<OrderRepayValidateResult> response);
        void onRepayValidateFailure(String msg);


    }

}