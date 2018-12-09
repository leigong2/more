package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.AddSignInter;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.SignList;
import com.moreclub.moreapp.main.model.SignSpaceList;

import java.util.List;

import retrofit2.Response;

/**
 * Created by Captain on 2017/5/2.
 */
@Implement(MerchantSignInteractSquareLoader.class)
public interface IMerchantSignInteractSquareLoader<T> {

    void onLoadSignList(String mid, String uid, String pn, String ps);

    interface LoadMerchantSignInteractSquareDataBinder<T> extends DataBinder {

        void onSignListResponse(RespDto<SignSpaceList> response);

        void onSignListFailure(String msg);
    }
}
