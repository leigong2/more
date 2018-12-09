package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserPrivilege;

/**
 * Created by Captain on 2017/4/9.
 */
@Implement(UserPrivilegeLoader.class)
public interface IUserPrivilegeLoader<T> {

    void loadPrivilege(String cardType,String city);

    interface UserPrivilegeDataBinder<T> extends DataBinder{
        void onUserPrivilegeResponse(RespDto<UserPrivilege> response);

        void onUserPrivilegeFailure(String msg);
    }

}
