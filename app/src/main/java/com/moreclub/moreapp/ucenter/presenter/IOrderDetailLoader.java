package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.DetailOrder;

import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/17.
 */

@Implement(OrderDetailLoader.class)
public interface IOrderDetailLoader<T> {
    void getPkgOrderDetail(long orderId, long uid);

    interface LoaderOrderDetailDataBinder<T> extends DataBinder {
        void onOrderResponse(Response<RespDto<DetailOrder>> response);

        void onOrderFailure(String msg);
    }

}
