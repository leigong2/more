package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-26.
 */

@Implement(UserCollectActivityLoader.class)
public interface IUserCollectActivityLoader {

    void loadActivity(String uid, String type, String pageIndex);

    interface LoadUserCollectActivityDataBinder<T> extends DataBinder {

        void onUserCollectActivityResponse(RespDto<ArrayList<CollectlistInfo>> response);

        void onUserCollectActivityFailure(String msg);

    }
}
