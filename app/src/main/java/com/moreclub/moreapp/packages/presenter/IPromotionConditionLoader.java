package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.PayBillOrder;
import com.moreclub.moreapp.packages.model.PayBillOrderParam;
import com.moreclub.moreapp.packages.model.UserMileage;
import com.moreclub.moreapp.ucenter.model.RateCardInfo;
import com.moreclub.moreapp.ucenter.model.UserCoupon;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/13.
 */
@Implement(PromotionConditionLoader.class)
public interface IPromotionConditionLoader<T> {

    void loadUserPromotionCondition(String mid,String uid);

    void loadBillOrder(PayBillOrderParam orderParam);

    void loadUserSupportCoupons(String uid,String mid);

    void loadMileage();

    interface LoaderPromotionConditionDataBinder<T> extends DataBinder{
        void onPromotionConditionResponse(RespDto<RateCardInfo> response);
        void onPromotionConditionFailure(String msg);

        void onPayBillOrderResponse(RespDto<PayBillOrder> response);
        void onPayBillOrderFailure(String msg);

        void onUserSupportCouponsResponse(RespDto<ArrayList<UserCoupon>> response);
        void onUserSupportCouponsFailure(String msg);

        void onMileageResponse(RespDto<UserMileage> response);
        void onMileageFailure(String msg);
    }
}
