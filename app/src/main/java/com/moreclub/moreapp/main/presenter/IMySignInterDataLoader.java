package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;

/**
 * Created by Administrator on 2017-09-14-0014.
 */

@Implement(MySignInterDataLoader.class)
public interface IMySignInterDataLoader {

    void onLoadUserInter(Long uid,Integer pn,Integer ps);

    interface LoadMySignInter extends DataBinder {
        void onUserInterResponse(RespDto respDto);

        void onUserInterFailure(String msg);
    }
}
