package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/9.
 */
@Implement(UserCollectMerchantLoader.class)
public interface IUserCollectMerchantLoader<T> {

 
    void loadMerchant(String uid,String type,String pageIndex);
 
    interface LoadUserCollectMerchantDataBinder<T> extends DataBinder{
        void onUserCollectMerchantResponse(RespDto<ArrayList<CollectlistInfo>> response);
        void onUserCollectMerchantFailure(String msg);
    }
}
