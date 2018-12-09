package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserPackageEvaluate;

/**
 * Created by Captain on 2017/4/1.
 */
@Implement(UserEvaluatePackageLoader.class)
public interface IUserEvaluatePackageLoader<T> {

    void userEvaluatePackage(UserPackageEvaluate param);

    interface LoaderUserEvaluatePackage<T> extends DataBinder{

        void onUserEvaluatePackageResponse(RespDto<String> response);

        void onUserEvaluatePackageFailure(String msg);
    }

}
