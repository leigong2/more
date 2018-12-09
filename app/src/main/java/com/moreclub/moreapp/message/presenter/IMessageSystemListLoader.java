package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.MessageSystem;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/5/4.
 */
@Implement(MessageSystemListLoader.class)
public interface IMessageSystemListLoader<T> {

    void loadMessageSystem(String uid,String platform,String type,String pageIndex,String pageSize);

    interface LoaderMessageSystemListDataBinder<T> extends DataBinder{
        void onMessageSystemListResponse(RespDto<ArrayList<MessageSystem>> response);
        void onMessageSystemListFailure(String msg);
    }
}
