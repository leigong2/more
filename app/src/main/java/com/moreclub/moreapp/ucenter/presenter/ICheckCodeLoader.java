package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.BindedUser;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserLogin;
import com.moreclub.moreapp.ucenter.model.UserLoginInfo;
import com.moreclub.moreapp.ucenter.model.UserQueryQuestion;
import com.moreclub.moreapp.ucenter.model.UserSecurityQuestion;

/**
 * Created by Captain on 2017/3/17.
 */
@Implement(CheckCodeLoader.class)
public interface ICheckCodeLoader<T> {

    void userGetSMSCode(String phone,String appkey,String sign);

    void userCheckSMSCode(String phone, String vcode, String appkey, String sign);

    void userGetSMSCodeFp1(String phone, String appkey, String sign, String fp);

    void userCheckSMSCodeFp(String phone, String vcode, String appkey, String sign, String fp);

    void userGetSecurityQuestion(UserQueryQuestion user, String open);

    void onReg(UserLogin regParam);

    void userCheckBindedPhone(String phone);

    interface LoadCheckCodeBinder<T> extends DataBinder {

        void onUseGetSMSCodeResponse(RespDto<String> response);

        void onUseGetSMSCodeFailure(String msg);

        void onUseCheckSMSCodeResponse(RespDto<String> response);

        void onUseCheckSMSCodeFailure(String msg);

        void onUserGetSecurityQuestionResponse(RespDto<UserSecurityQuestion> response);

        void onUserGetSecurityQuestionFailure(String msg);

        void onUserRegResponse(RespDto<UserLoginInfo> response);

        void onUserRegFailure(String msg);

        void onUseCheckBindedPhoneResponse(RespDto<BindedUser> response);

        void onUseCheckBindedPhoneCodeFailure(String msg);
    }
}
