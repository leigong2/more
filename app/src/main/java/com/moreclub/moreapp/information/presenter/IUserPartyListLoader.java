package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-26-0026.
 */

@Implement(UserPartyListLoader.class)
public interface IUserPartyListLoader {

    void onLoadPartyList(Integer mid, Long uid, Integer pn, Integer ps);

    interface LoadUserPartyListBinder extends DataBinder {

        void onGetPartyListResponse(RespDto<List<MerchantActivity>> response);

        void onGetPartyListFaiure(String msg);
    }
}
