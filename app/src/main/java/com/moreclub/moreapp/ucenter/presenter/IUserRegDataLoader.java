package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;

/**
 * Created by Captain on 2017/3/17.
 */
@Implement(UserRegDataLoader.class)
public interface IUserRegDataLoader<T> {

    void onReg(UserLogin regParam);

    interface UserRegDataBinder<T> extends DataBinder{
        void onUserRegResponse(RespDto<UserLoginInfo> response);

        void onUserRegFailure(String msg);
    }

}
