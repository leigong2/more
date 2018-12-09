package com.moreclub.moreapp.chat.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.chat.model.ChatGroupUser;
import com.moreclub.moreapp.information.model.HxRoomDetails;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/6/9.
 */
@Implement(ChatGroupDetailsLoader.class)
public interface IChatGroupDetailsLoader<T> {

    void loadChatGroupUser(String roomID,String pageIndex,String pageSize);

    void loadRoomHxDetails(String roomId);

    void onExitChatGroup(String uid,String roomId);

    interface LoaderChatGroupDetailsDataBinder<T> extends DataBinder{

        void onChatGroupUserResponse(RespDto<ArrayList<ChatGroupUser>> response);
        void onChatGroupUserFailure(String msg);

        void onRoomHxDetailsResponse(RespDto<HxRoomDetails> response);
        void onRoomHxDetailsFaiure(String msg);

        void onExitChatGroupResponse(RespDto<HxRoomDetails> response);
        void onExitChatGroupFaiure(String msg);
    }

}
