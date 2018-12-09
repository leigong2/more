package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017-05-26.
 */

@Implement(UserCollectHeadlineLoader.class)
public interface IUserCollectHeadlineLoader {

    void loadHeadline(String uid,String type,String pageIndex);

    interface LoadUserCollectHeadlineDataBinder<T> extends DataBinder {

        void onUserCollectHeadlineResponse(RespDto<ArrayList<CollectlistInfo>> response);

        void onUserCollectHeadlineFailure(String msg);

    }
}
