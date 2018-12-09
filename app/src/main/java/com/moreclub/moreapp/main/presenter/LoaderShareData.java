package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.ShareParam;
import com.moreclub.moreapp.main.model.ShareRequestResult;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/4/20.
 */

public class LoaderShareData extends BasePresenter<IShareDataLoader.LoadShareDataBinder> implements IShareDataLoader {
    @Override
    public void share(ShareParam param) {

        Callback<RespDto<ShareRequestResult>> callback = new Callback<RespDto<ShareRequestResult>>() {
            @Override
            public void onResponse(Call<RespDto<ShareRequestResult>> call, Response<RespDto<ShareRequestResult>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onShareFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onShareResponse(response.body());
                }

            }

            @Override
            public void onFailure(Call<RespDto<ShareRequestResult>> call, Throwable t) {
                if (getBinder()!=null)
                    getBinder().onShareFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onShare(param).enqueue(callback);
    }
}
