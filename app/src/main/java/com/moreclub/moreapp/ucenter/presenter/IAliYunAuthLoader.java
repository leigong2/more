package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.AliYunAuth;
import com.moreclub.moreapp.ucenter.model.AliYunAuthParm;

/**
 * Created by Captain on 2017/3/18.
 */
@Implement(AliYunAuthLoader.class)
public interface IAliYunAuthLoader<T> {

    void imageAuto(AliYunAuthParm parm);
    void imageAutoOpen(AliYunAuthParm parm);

    interface loaderAliYunAuthBinder<T> extends DataBinder{

        void onAliYunAuthResponse(RespDto<AliYunAuth> response);

        void onAliYunAuthFailure(String msg);
    }
}
