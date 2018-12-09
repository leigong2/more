package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.MessageWallReply;
import com.moreclub.moreapp.information.model.UserWriteMessageWall;

/**
 * Created by Administrator on 2017-06-06.
 */

@Implement(MessageWallReplyLoader.class)
public interface IMessageWallReplyLoader {

    void onLoadMessageWallReply(Long cid,Integer pn,Integer ps);

    void onPostMessageWallReply(String token,UserWriteMessageWall user);

    interface LoadMessageWallReplyBinder extends DataBinder {

        void onLoadReplyResponse(RespDto<MessageWallReply> response);

        void onLoadReplyFailure(String msg);

        void onPostReplyResponse(RespDto<String> body);

        void onPostReplyFailure(String msg);

    }
}
