package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.FollowsUser;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/6/1.
 */
@Implement(MessageFollowMerchantListLoader.class)
public interface IMessageFollowMerchantListLoader<T>  {
    void loadFollowsMerchant(String uid,String type,String pn,int followType);
    interface LoaderMessageFollowMerchantDataBinder<T> extends DataBinder {
        void onMessageFollowMerchantResponse(RespDto<ArrayList<FollowsUser>> response);
        void onMessageFollowMerchantFailure(String msg);
    }
}
