package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.ucenter.model.Topics;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/28.
 */
@Implement(MChannelRecommListLoader.class)
public interface IMChannelRecommListLoader {

    void onLoadTopicList(int pn,int ps,int city,int topic,long uid);
    void loadMChannelRecomm(long uid,int city,int page,int pageSize);

    interface LoadMChannelRecommListDataBinder extends DataBinder {
        void onMChannelRecommResponse(RespDto<ArrayList<MainChannelItem>> body);
        void onMChannelRecommFailure(String message);

        void onLoadTopicListResponse(RespDto<Topics> body);
        void onLoadTopicListFailure(String message);
    }

}
