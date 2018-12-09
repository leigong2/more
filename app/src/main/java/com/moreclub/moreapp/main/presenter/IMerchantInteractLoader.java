package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantsSignInters;
import com.moreclub.moreapp.main.model.SignList;

import java.util.List;

import retrofit2.Response;

/**
 * Created by Captain on 2017/5/2.
 */
@Implement(MerchantInteractLoader.class)
public interface IMerchantInteractLoader<T> {

    void onLoadOpenMerchantInters(String token, Integer mid, Integer pn);

    interface LoadMerchantInteractDataBinder<T> extends DataBinder {

        void onOpenMerchantIntersResponse(Response<List<MerchantsSignInters>> response);

        void onOpenMerchantIntersFailure(String msg);
    }
}
