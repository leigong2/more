package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.BannerItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

public class MChannelFragmentDataLoader extends BasePresenter<IMChannelFragmentDataLoader.LoadMChannelFragmentDataBinder>
        implements IMChannelFragmentDataLoader {
    @Override
    public void onLoadBanner(int cityId, String pos, int type) {
        Callback callback = new Callback<RespDto<List<BannerItem>>>() {
            @Override
            public void onResponse(Call<RespDto<List<BannerItem>>> call,
                                   Response<RespDto<List<BannerItem>>> response) {
                if (response != null && response.body() != null && response.body().isSuccess())
                    if (getBinder() != null)
                        getBinder().onBannerResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<List<BannerItem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onBannerFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadBanner(cityId, pos, type).enqueue(callback);
    }
}
