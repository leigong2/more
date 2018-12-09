package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/7/28.
 */
@Implement(MChannelAttentionListLoader.class)
public interface IMChannelAttentionListLoader {

    void loadMChannelAttention(long uid,int page,int pageSize);

    interface LoadMChannelAttentionListDataBinder extends DataBinder {

        void onMChannelAttentionResponse(RespDto<ArrayList<MainChannelItem>> body);
        void onMChannelAttentionFailure(String message);

    }
}
