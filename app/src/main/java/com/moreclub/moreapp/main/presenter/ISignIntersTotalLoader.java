package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;

import java.util.List;

/**
 * Created by Administrator on 2017-09-12-0012.
 */

@Implement(SignIntersTotalLoader.class)
public interface ISignIntersTotalLoader {

    void onLoadSignIntersTotal(String city,Integer pn);

    interface LoadSignIntersTotalDataBinder<MerchantsSignInters> extends DataBinder {

        void onSignIntersTotalResponse(RespDto<List<MerchantsSignInters>> respDto);

        void onSignIntersTotalFailure(String msg);

    }
}
