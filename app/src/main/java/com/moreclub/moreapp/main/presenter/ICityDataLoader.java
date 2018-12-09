package com.moreclub.moreapp.main.presenter;


import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.model.City;
import com.moreclub.moreapp.main.model.TagDict;

import java.util.List;

/**
 * 加载商圈数据的逻辑
 */

@Implement(CityDataLoader.class)
public interface ICityDataLoader<T> {
    void onLoadCity();

    interface CityDataBinder<T> extends DataBinder {
        void onCityResponse(List<T> response);

        void onCityFailure(String msg);
    }
}