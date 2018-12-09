package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;

/**
 * Created by Administrator on 2017-05-16.
 */
@Implement(HeadlineLoader.class)
public interface IHeadlineLoader {

    void onLoadHeadlineList(Integer cityId, Integer pageIndex, Integer pageSize);

    interface LoadHeadlineBinder extends DataBinder {

        void onGetHeadlineResponse(RespDto<Headline> response);

        void onGetHeadlineFailure(String msg);
    }
}
