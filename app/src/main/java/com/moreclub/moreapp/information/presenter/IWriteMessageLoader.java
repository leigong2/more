package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;

import java.util.List;

/**
 * Created by Administrator on 2017-06-01.
 */

@Implement(WriteMessageLoader.class)

public interface IWriteMessageLoader {

    void onLoadMessageWall(Integer actId,Integer pn,Integer ps);

    void onPostMessageWall(String token,UserWriteMessageWall user);

    interface WriteMessageBinder extends DataBinder {

        void writeMessageResponse(RespDto<String> body);

        void writeMessageFailure(String msg);

        void onMessageWallResponse(RespDto<List<MessageWall>> body);

        void onMessageWallFailure(String msg);

    }
}
