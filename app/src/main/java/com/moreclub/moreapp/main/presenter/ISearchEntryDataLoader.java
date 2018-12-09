package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.BannerItem;
import com.moreclub.moreapp.main.model.BizArea;
import com.moreclub.moreapp.main.model.MerchantItem;
import com.moreclub.moreapp.main.model.TagDict;

import java.util.List;

/**
 * 加载商圈数据的逻辑
 */

@Implement(SearchEntryDataLoader.class)
public interface ISearchEntryDataLoader<T> {
    void onLoadSearchTag();

    interface SerchEntryDataBinder<T> extends DataBinder {
        void onSearchTagResponse(RespDto<List<TagDict>> response);

        void onSearchTagFailure(String msg);
    }
}