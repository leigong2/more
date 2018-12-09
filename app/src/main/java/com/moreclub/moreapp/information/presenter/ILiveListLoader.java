package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;

/**
 * Created by Administrator on 2017-05-18.
 */

@Implement(LiveListLoader.class)
public interface ILiveListLoader {

    void onLoadLiveList(String cityCode, Integer pn, Integer ps, Integer type, Long uid);

    interface LoadLiveListBinder extends DataBinder {

        void onGetLiveListResponse(RespDto<ActivityData> response);

        void onGetLiveListFaiure(String msg);
    }
}
