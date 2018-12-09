package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.RepayValidateParam;
import com.moreclub.moreapp.util.MoreUser;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Captain on 2017/6/23.
 */

public class RepayValidateLoader extends BasePresenter<IRepayValidateLoader.LoaderRepayValidateBinder> implements IRepayValidateLoader {
    @Override
    public void onRepayValidate(RepayValidateParam param) {

        Callback callback = new Callback<RespDto<Boolean>>() {
            @Override
            public void onResponse(Call<RespDto<Boolean>> call,
                                   Response<RespDto<Boolean>> response) {

                if (response!=null&&response.code() == 401){
                    if (getBinder() != null)
                        getBinder().onRepayValidateFailure("401");
                    return;
                }
                if (response.body().isSuccess()){
                    if (getBinder() != null)
                        getBinder().onRepayValidateResponse(response.body());
                } else {
                    if (getBinder() != null)
                        getBinder().onRepayValidateFailure("C10110");
                }
            }

            @Override
            public void onFailure(Call<RespDto<Boolean>> call, Throwable t) {
                if (getBinder() != null)
                    getBinder().onRepayValidateFailure(t.getMessage());
            }
        };
        String token = MoreUser.getInstance().getAccess_token();
        ApiInterface.ApiFactory.createApi(token).onRepayValidate(param).enqueue(callback);

    }
}
