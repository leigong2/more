package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.packages.model.PackageOrder;
import com.moreclub.moreapp.packages.model.PackageOrderResult;
import com.moreclub.moreapp.ucenter.model.UserCoupon;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/3/27.
 */
@Implement(PackageConfirmOrderLoader.class)
public interface IPackageConfirmOrderLoader<T> {


    void packageOrder(PackageOrder param);
    void loadUserSupportCoupons(String uid,String mid);
    interface LoaderPackageConfirmOrder<T> extends DataBinder{
        void onPackageOrderResponse(RespDto<PackageOrderResult> response);
        void onPackageOrderFailure(String msg);

        void onUserSupportCouponsResponse(RespDto<ArrayList<UserCoupon>> response);
        void onUserSupportCouponsFailure(String msg);
    }

}
