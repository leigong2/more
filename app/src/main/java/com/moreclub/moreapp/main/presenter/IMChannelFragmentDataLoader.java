package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.BannerItem;

import java.util.List;

/**
 * Created by Administrator on 2017-09-08-0008.
 */

@Implement(MChannelFragmentDataLoader.class)
public interface IMChannelFragmentDataLoader {
    void onLoadBanner(int cityId, String pos, int type);

    interface LoadMChannelFragmentDataBinder extends DataBinder {
        void onBannerResponse(RespDto<List<BannerItem>> response);

        void onBannerFailure(String msg);
    }
}
