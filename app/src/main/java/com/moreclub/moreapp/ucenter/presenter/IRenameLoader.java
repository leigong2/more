package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserInfo;
import com.moreclub.moreapp.ucenter.model.UserPassword;

/**
 * Created by Administrator on 2017-05-12.
 */

@Implement(RenameLoader.class)
public interface IRenameLoader {

    void onRenamePwd(UserPassword regParam,String token);

    interface RenameBinder<T> extends DataBinder {

        void onRenameResponse(RespDto<UserPassword> response);

        void onRenameFailure(String msg);
    }
}
