package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.UserSignSetting;
import com.moreclub.moreapp.ucenter.model.AutoSigninSettings;

/**
 * Created by Captain on 2017/5/26.
 */
@Implement(UserSecretLoader.class)
public interface IUserSecretLoader<T> {

    void onSecretSign(String uid, AutoSigninSettings param);
    void loadSignSetting(String uid);

    interface LoaderUserSecretDataBinder extends DataBinder{

        void onSecretSignResponse(RespDto<String> response);
        void onSecretSignFailure(String msg);

        void onSignSettingResponse(RespDto<UserSignSetting> response);
        void onSignSettingFailure(String msg);

    }
}
