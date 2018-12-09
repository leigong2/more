package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
/**
 * Created by Administrator on 2017-09-08-0008.
 */

@Implement(SpaceV3DataLoader.class)
public interface ISpaceV3DataLoader {

    void onLoadSpaceV3Tabs();

    interface LoadSpaceViewV3DataBinder<SignSpaceList> extends DataBinder {

        void onLoadSpaceV3TabsResponse(RespDto<SignSpaceList> respDto);

        void onLoadSpaceV3TabsFailure(String msg);

    }
}
