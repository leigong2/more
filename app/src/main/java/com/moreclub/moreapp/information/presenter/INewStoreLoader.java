package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.NewStore;

/**
 * Created by Administrator on 2017-05-16.
 */
@Implement(NewStoreLoader.class)
public interface INewStoreLoader {

    void onLoadNewStoreList(String cityCode, Integer pageIndex, Integer pageSize);

    interface LoadNewStoreBinder extends DataBinder {

        void onGetNewStoreResponse(RespDto<NewStore> response);

        void onGetNewStoreFailure(String msg);
    }
}
