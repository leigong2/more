package com.moreclub.moreapp.main.presenter;


import android.content.Context;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;

import java.util.List;

/**
 * 加载闪屏页的逻辑
 */

@Implement(SplashDataLoader.class)
public interface ISplashDataLoader<T> {
    void onLoadSplash(final Context context);

    interface SplashDataBinder<T> extends DataBinder {
        void onSplashResponse(T response);

        void onSplashFailure(String msg);
    }
}