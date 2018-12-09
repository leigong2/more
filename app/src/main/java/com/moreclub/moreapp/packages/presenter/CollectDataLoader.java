package com.moreclub.moreapp.packages.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.CollectParam;
import com.moreclub.moreapp.packages.api.ApiInterface;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/3/31.
 */

public class CollectDataLoader extends BasePresenter<ICollectDataLoader.LoaderCollectDataBinder> implements ICollectDataLoader {
    @Override
    public void collect(CollectParam param) {
        Callback callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call,
                                   Response<RespDto<String>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onCollectFailure("401");
                    return;
                }

                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onCollectResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onCollectFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onCollectData(param).enqueue(callback);
    }
}
