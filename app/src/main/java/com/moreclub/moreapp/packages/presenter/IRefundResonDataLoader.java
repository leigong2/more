package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.CollectParam;
import com.moreclub.moreapp.packages.model.DictionaryName;
import com.moreclub.moreapp.packages.model.PkgRefundReq;

import java.util.List;

import retrofit2.Response;


/**
 * Created by Captain on 2017/3/31.
 */
@Implement(RefundReasonDataLoader.class)
public interface IRefundResonDataLoader<T> {

    void getRefundReson();

    void doPkgRefund(PkgRefundReq req);

    interface LoaderReasonDataBinder<T> extends DataBinder{

        void onPkgRefundResponse(Response<RespDto<Long>> response);

        void onPkgRefundFailure(String msg);

        void onResponse(RespDto<List<DictionaryName>> response);

        void onFailure(String msg);
    }
}
