package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.BindedUser;
import com.moreclub.moreapp.ucenter.model.UserBindPhone;
import com.moreclub.moreapp.ucenter.model.UserInfo;

/**
 * Created by Administrator on 2017-05-12.
 */

@Implement(BindPhoneLoader.class)
public interface IBindPhoneLoader<T> {
    void userGetSMSCodeBindPhone(String phone,String appkey,String sign,String fp);
    void userCheckSMSCode(String phone,String vcode,String appkey,String sign);
    void userCheckBindedPhone(String phone);
    void onBindPhone(String token,UserBindPhone regParam);

    interface LoadCheckCodeBinder<T> extends DataBinder {
        void onUseGetSMSCodeResponse(RespDto<String> response);

        void onUseGetSMSCodeFailure(String msg);

        void onUseCheckSMSCodeResponse(RespDto<String> response);

        void onUseCheckSMSCodeFailure(String msg);

        void onUseCheckBindedPhoneResponse(RespDto<BindedUser> response);

        void onUseCheckBindedPhoneCodeFailure(String msg);

        void onBindResponse(RespDto<UserBindPhone> response);

        void onBindFailure(String msg);
    }
}
