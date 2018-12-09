package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.ui.Presenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.UserPassword;
import com.moreclub.moreapp.ucenter.presenter.IRenameLoader.RenameBinder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-12.
 */

public class RenameLoader extends BasePresenter<RenameBinder> implements IRenameLoader {
    @Override
    public void onRenamePwd(UserPassword regParam,String token) {
        Callback<RespDto<String>> callBack = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (getBinder() != null && response.body() != null && response.body().isSuccess()) {
                    getBinder().onRenameResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onRenameFailure(response.body().getErrorDesc());
                    } else {
                        getBinder().onRenameFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                getBinder().onRenameFailure(t.getMessage());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onRenamePwd(regParam).enqueue(callBack);
    }
}
