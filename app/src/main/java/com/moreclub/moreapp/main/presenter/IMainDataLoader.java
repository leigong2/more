package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.ActivityData;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.HeadlineCount;
import com.moreclub.moreapp.main.model.MainChannelItem;
import com.moreclub.moreapp.main.model.MerchantCount;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.MessageCenterPush;
import com.moreclub.moreapp.main.model.PackageResp;
import com.moreclub.moreapp.main.model.SignInterUser;
import com.moreclub.moreapp.main.model.TagDict;
import com.moreclub.moreapp.main.model.UserSignParam;
import com.moreclub.moreapp.main.model.UserSignResult;
import com.moreclub.moreapp.main.model.UserSignSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * 加载商圈数据的逻辑
 */

@Implement(MainDataLoader.class)
public interface IMainDataLoader<T> {
    void onLoadBizArea(String city);

    void onLoadFeatureTag();

    void onLoadBanner(int cityId, String pos, int type);

    void onLoadMerchants(String city, int page, int size);

    void onLoadPkgs(String city, long uid, int page, int size);

    void onLoadHeadlineCount();

    void onLoadMerchantCount(String cityCode,Integer cityId);

    void onLoadLiveList(String cityCode, Integer pn, Integer ps, Integer type, Long uid);

    void onLoadMainChannelList(Long uid,Integer city,Integer page,Integer pageSize);

    void onLoadSignInterUser(String city);



//    /v1/signinter/users/{city}
    interface LoadMainDataBinder<T> extends DataBinder {
        void onBizAreaResponse(RespDto<List<BizArea>> response);

        void onBizAreaFailure(String msg);

        void onFeatureTagResponse(RespDto<List<TagDict>> response);

        void onFeatureTagFailure(String msg);

        void onBannerResponse(RespDto<List<BannerItem>> response);

        void onBannerFailure(String msg);

        void onMerchantsResponse(RespDto<List<MerchantItem>> response);

        void onMerchantsFailure(String msg);

        void onPkgResponse(RespDto<PackageResp> response);

        void onPkgFailure(String msg);

        void onHeadlineCountResponse(RespDto<HeadlineCount> response);

        void onHeadlineCountFailure(String msg);

        void onMerchantCountResponse(RespDto<MerchantCount> response);

        void onMerchantCountFailure(String msg);

        void onGetLiveListResponse(RespDto<ActivityData> response);

        void onGetLiveListFaiure(String msg);

        void onGetChannelListResponse(RespDto<ArrayList<MainChannelItem>> response);

        void onGetChannelListFaiure(String msg);

        void onGetSignInterResponse(RespDto<ArrayList<SignInterUser>> response);
        void onGetSignInterFaiure(String msg);

    }
}