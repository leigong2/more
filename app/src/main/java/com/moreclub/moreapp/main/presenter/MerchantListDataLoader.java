package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.util.MoreUser;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/2/27.
 */

public class MerchantListDataLoader extends BasePresenter<IMerchantListDataLoader.
        LoadMerchantListDataBinder> implements IMerchantListDataLoader {

    @Override
    public void onLoadMerchantListData(String query, String city,int type,
                                       int pageNum, int pageSize) {
        Callback callback = new Callback<RespDto<List<MerchantItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<MerchantItem>>> call,
                                   Response<RespDto<List<MerchantItem>>> response) {
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onMerchantListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<MerchantItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMerchantFailure(t.getMessage());
            }
        };

        if (MoreUser.getInstance().getUid()!=null&&MoreUser.getInstance().getUid()>0){
            ApiInterface.ApiFactory.createApi().onLoadMerchantList(query,city,
                    type, pageNum, pageSize,MoreUser.getInstance().getUid()).enqueue(callback);
        } else {
            ApiInterface.ApiFactory.createApi().onLoadMerchantListNoUid(query,city,
                    type, pageNum, pageSize).enqueue(callback);
        }

    }
}
