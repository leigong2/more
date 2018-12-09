package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserCoupon;

/**
 * Created by Captain on 2017/6/22.
 */
@Implement(UserCouponDetailsLoader.class)
public interface IUserCouponDetailsLoader <T> {

    void onLoadUserCouponDetails(String cid);

    interface LoadUserCouponDetailsDataBinder<T> extends DataBinder {

        void onUserCouponDetailsResponse(RespDto<UserCoupon> response);
        void onUserCouponDetailsFailure(String msg);

    }

}
