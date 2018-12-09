package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.UpdateBody;
import com.moreclub.moreapp.main.model.UpdateResp;

/**
 * Created by Administrator on 2017-09-12-0012.
 */

@Implement(SuperMainDataLoader.class)
public interface ISuperMainDataLoader {

    void onLoadRedPointer(String uid, String platform);

    void onUpdateMore(UpdateBody body);

    interface LoadSuperMainDataBinder extends DataBinder {

        void onRedPointerResponse(RespDto<MessageCenterPush> response);

        void onRedPointerFailure(String msg);

        void onUpdateResponse(RespDto<UpdateResp> response);

        void onUpdateFailure(String msg);
    }
}
