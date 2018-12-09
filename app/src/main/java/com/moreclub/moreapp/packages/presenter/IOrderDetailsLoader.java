package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserOrderCancelParam;

/**
 * Created by Captain on 2017/8/14.
 */
@Implement(OrderDetailsLoader.class)
public interface IOrderDetailsLoader {

    void delOrder(String orderID);
    void cancelOrder(UserOrderCancelParam param);

    interface LoaderOrderDetailsDataBinder extends DataBinder{

        void onDelOrderResponse(RespDto<String> response);
        void onDelOrderFailure(String msg);

        void onCancelOrderResponse(RespDto<String> response);
        void onCancelOrderFailure(String msg);

    }
}
