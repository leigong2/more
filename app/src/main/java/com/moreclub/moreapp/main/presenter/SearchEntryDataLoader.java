package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.TagDict;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/2/23.
 */

public class SearchEntryDataLoader extends BasePresenter<ISearchEntryDataLoader.SerchEntryDataBinder>
        implements ISearchEntryDataLoader {

    @Override
    public void onLoadSearchTag() {
        Callback callback = new Callback<RespDto<List<TagDict>>>() {
            @Override
            public void onResponse(Call<RespDto<List<TagDict>>> call,
                                   Response<RespDto<List<TagDict>>> response) {
                if (response.body().isSuccess()) {
                    if (getBinder() != null) {
                        getBinder().onSearchTagResponse(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<List<TagDict>>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onSearchTagFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSearchTag().enqueue(callback);
    }

}


