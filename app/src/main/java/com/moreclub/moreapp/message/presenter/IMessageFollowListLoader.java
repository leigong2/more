package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.FollowsUser;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/6/1.
 */
@Implement(MessageFollowListLoader.class)
public interface IMessageFollowListLoader<T> {

    void loadFollowsUser(String uid,String type,String pn,int followType);

    interface LoaderMessageFollowDataBinder<T> extends DataBinder{
        void onFollowsUserResponse(RespDto<ArrayList<FollowsUser>> response);
        void onFollowsUserFailure(String msg);
    }
}
