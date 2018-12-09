package com.moreclub.moreapp.main.presenter;


import android.content.Context;

import com.moreclub.common.ui.BasePresenter;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.app.MainApp;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.main.api.ApiInterface;
import com.moreclub.moreapp.main.model.SplashImage;
import com.moreclub.moreapp.util.MoreUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.moreclub.moreapp.main.constant.Constants.KEY_CITY_CODE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_EXPIRE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_IMAGE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_LINK;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_TITLE;
import static com.moreclub.moreapp.main.constant.Constants.KEY_SPLASH_TYPE;

/**
 * Created by Administrator on 2017/2/23.
 */

public class SplashDataLoader extends BasePresenter<ISplashDataLoader.SplashDataBinder>
        implements ISplashDataLoader {

    @Override
    public void onLoadSplash(final Context context) {
        Callback callback = new Callback<RespDto<SplashImage>>() {
            @Override
            public void onResponse(Call<RespDto<SplashImage>> call,
                                   Response<RespDto<SplashImage>> response) {
                if (response == null || response.body() == null) return;

                if (response.body().isSuccess()) {
                    SplashImage splashImage = response.body().getData();
                    String imageUrl = PrefsUtils.getString(context.getApplicationContext(),
                            KEY_SPLASH_IMAGE, "");
                    Long lastExpire = PrefsUtils.getLong(context.getApplicationContext(),
                            KEY_SPLASH_EXPIRE, 0);

                    if (splashImage != null) {
                        Long expireTime = splashImage.getExpire();
                        String currentImageUrl = splashImage.getImageUrl();
                        if (!lastExpire.equals(expireTime) || !imageUrl.equals(currentImageUrl)) {
                            if (expireTime!=null) {
                                PrefsUtils.getEditor(context.getApplicationContext())
                                        .putString(KEY_SPLASH_TITLE, splashImage.getTitle())
                                        .putString(KEY_SPLASH_IMAGE, splashImage.getImageUrl())
                                        .putInt(KEY_SPLASH_TYPE, splashImage.getClickType())
                                        .putString(KEY_SPLASH_LINK, splashImage.getClickLink())
                                        .putLong(KEY_SPLASH_EXPIRE, expireTime)
                                        .commit();
                            }
                        }
                    } else {
                        PrefsUtils.getEditor(context.getApplicationContext())
                                .remove(KEY_SPLASH_TITLE)
                                .remove(KEY_SPLASH_IMAGE)
                                .remove(KEY_SPLASH_TYPE)
                                .remove(KEY_SPLASH_LINK)
                                .remove(KEY_SPLASH_EXPIRE)
                                .commit();
                    }

                    if (getBinder() != null) {
                        getBinder().onSplashResponse(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<SplashImage>> call, Throwable t) {
                if (getBinder() != null) {
                    getBinder().onSplashFailure(t.getMessage());
                }
            }
        };
        ApiInterface.ApiFactory.createApi().onLoadSplashImage(PrefsUtils.getString(context,KEY_CITY_CODE,"cd")).enqueue(callback);
    }

}


