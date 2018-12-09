package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserCenter;
import com.moreclub.moreapp.ucenter.model.UserCoupon;
import com.moreclub.moreapp.ucenter.model.UserCouponResult;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/12.
 */
@Implement(UserCenterLoader.class)
public interface IUserCenterLoader<T> {

    void loadUserCenter(String uid);
    void loadUserCoupon(String uid);
    void onShareCallBack(String uid);
    interface LoaderUserCenterDataBinder<T> extends DataBinder{
        void onUserCenterResponse(RespDto<UserCenter> response);
        void onUserCenterFailure(String msg);

        void onUserCouponResponse(RespDto<UserCouponResult> response);
        void onUserCouponFailure(String msg);

        void onShareCallBackResponse(RespDto<Object> response);
        void onShareCallBackFailure(String msg);
    }
}
