package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserTag;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-07-18-0018.
 */

public class JobTagDataLoader extends BasePresenter<IJobTagLoader.JobTagDataBinder>
        implements IJobTagLoader {
    @Override
    public void loadTag(String modal) {
        Callback<RespDto<Map<String, List<UserTag>>>> callback = new Callback<RespDto<Map<String, List<UserTag>>>>() {
            @Override
            public void onResponse(Call<RespDto<Map<String, List<UserTag>>>> call, Response<RespDto<Map<String, List<UserTag>>>> response) {
                if (getBinder() != null && response != null && response.body() != null && response.body().isSuccess())
                    getBinder().onTagResponse(response.body());
            }

            @Override
            public void onFailure(Call<RespDto<Map<String, List<UserTag>>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onTagFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadUserTags(modal).enqueue(callback);
    }
}
