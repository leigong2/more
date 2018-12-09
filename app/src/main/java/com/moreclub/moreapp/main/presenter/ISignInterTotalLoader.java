package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.SignSpaceList;

/**
 * Created by Administrator on 2017-07-26-0026.
 */

@Implement(SignInterTotalLoader.class)
public interface ISignInterTotalLoader {

    void onLoadSignInterTotal(String city,Integer pn,Integer ps);

    interface LoadSignInterTotalDataBinder<SignSpaceList> extends DataBinder {

        void onSignInterTotalResponse(RespDto<SignSpaceList> respDto);

        void onSignInterTotalFailure(String msg);

    }
}
