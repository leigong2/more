package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.CollectlistInfo;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/4/9.
 */
@Implement(UserCollectPackageLoader.class)
public interface IUserCollectPackageLoader<T> {
	
    void loadPackage(String uid,String type,String pageIndex);

    interface LoadUserCollectPackageDataBinder<T> extends DataBinder{
        void onUserCollectPackageResponse(RespDto<ArrayList<CollectlistInfo>> response);
        void onUserCollectPackageFailure(String msg);
    }
	
}
