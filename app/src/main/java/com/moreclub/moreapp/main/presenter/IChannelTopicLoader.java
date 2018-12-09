package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.Topic;

import java.util.List;

/**
 * Created by Captain on 2017/8/26.
 */
@Implement(ChannelTopicLoader.class)
public interface IChannelTopicLoader {

    void loadTopic(int cityid);

    interface LoaderChannelTopicDataBinder extends DataBinder{

        void onTopicListResponse(RespDto<List<Topic>> response);
        void onTopicListFailure(String msg);

    }

}
