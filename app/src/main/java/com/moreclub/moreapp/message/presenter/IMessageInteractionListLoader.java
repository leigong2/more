package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.MessageInteraction;

import java.util.List;

/**
 * Created by Administrator on 2017-06-22.
 */

@Implement(MessageInteractionListLoader.class)
public interface IMessageInteractionListLoader {

    void loadInteractionMessage(String token,Long uid);

    interface LoaderMessageInteractionListDataBinder extends DataBinder {

        void onLoadInteractionResponse(RespDto<List<MessageInteraction>> body);

        void onLoadInteractionFailure(String msg);
    }
}
