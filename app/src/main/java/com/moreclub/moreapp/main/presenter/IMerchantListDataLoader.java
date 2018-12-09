package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.MerchantItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Captain on 2017/2/27.
 */
@Implement(MerchantListDataLoader.class)
public interface IMerchantListDataLoader<T>{

    void onLoadMerchantListData(String query, String city,int type, int pageNum, int pageSize);

    interface LoadMerchantListDataBinder<T> extends DataBinder {
        void onMerchantListResponse(RespDto<List<MerchantItem>> responce);
        void onMerchantFailure(String msg);
    }

}
