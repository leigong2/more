package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.AddUGCParam;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.Topic;
import com.moreclub.moreapp.ucenter.model.UploadedUGCChannel;

import java.util.List;

/**
 * Created by Captain on 2017/8/28.
 */
@Implement(PublishUGCLoader.class)
public interface IPublishUGCLoader {

    void loadNearbyMerchant(String city, Integer pn, Integer ps, String location);
    void loadTopicList(int cityid);
    void uploadUGC(AddUGCParam param);
    interface LoaderPublishUGCDataBinder<T> extends DataBinder {

        void onNearbyMerchantsResponse(RespDto<List<MerchantItem>> response);
        void onNearbyMerchantsFailure(String msg);

        void onTopicListResponse(RespDto<List<Topic>> response);
        void onTopicListFailure(String msg);

        void onUploadUGCResponse(RespDto<UploadedUGCChannel> response);
        void onUploadUGCFailure(String msg);
    }
}
