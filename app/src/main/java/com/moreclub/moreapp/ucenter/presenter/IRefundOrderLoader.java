package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.OrderResp;

import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/17.
 */

@Implement(RefoundOrderLoader.class)
public interface IRefundOrderLoader<T> {
    void getAllOrderList(String type, long uid, int pageNum, int pageSize);

    interface LoaderOrderDataBinder<T> extends DataBinder {
        void onOrderResponse(Response<RespDto<OrderResp>> response);
        void onOrderFailure(String msg);

        void onDelOrderResponse(RespDto<String> response);
        void onDelOrderFailure(String msg);
    }

}
