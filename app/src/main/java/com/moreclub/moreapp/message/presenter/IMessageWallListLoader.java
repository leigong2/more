package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.MessageWall;

import java.util.List;

/**
 * Created by Administrator on 2017-06-07.
 */

@Implement(MessageWallListLoader.class)
public interface IMessageWallListLoader {
    void loadSystemMessage(String token,String platform,Long uid,String type,Integer pageIndex,Integer pageSize);

    interface LoaderMessageWallListDataBinder extends DataBinder {

        void onLoadWallResponse(RespDto<List<MessageWall>> body);

        void onLoadWallFailure(String msg);
    }
}
