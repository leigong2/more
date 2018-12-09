package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.api.ApiInterface;
import com.moreclub.moreapp.message.model.MessageSystem;
import com.moreclub.moreapp.util.MoreUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/5/4.
 */

public class MessageSystemListLoader extends BasePresenter<IMessageSystemListLoader.LoaderMessageSystemListDataBinder> implements IMessageSystemListLoader {


    @Override
    public void loadMessageSystem(String uid,String platform,String type,String pageIndex,String pageSize) {

        Callback callback = new Callback<RespDto<ArrayList<MessageSystem>>>() {
            @Override
            public void onResponse(Call<RespDto<ArrayList<MessageSystem>>> call,
                                   Response<RespDto<ArrayList<MessageSystem>>> response) {
                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onMessageSystemListFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onMessageSystemListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<RespDto<ArrayList<MessageSystem>>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onMessageSystemListFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onLoadMessageList(uid,platform,type,pageIndex,pageSize).enqueue(callback);
    }
}
