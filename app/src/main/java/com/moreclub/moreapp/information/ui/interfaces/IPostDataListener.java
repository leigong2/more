package com.moreclub.moreapp.information.ui.interfaces;

import com.moreclub.moreapp.information.model.ActivityData;

/**
 * Created by Administrator on 2017-05-19.
 */

public interface IPostDataListener {

    void onResponse(ActivityData data);

    void onFailure();
}
