package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserLoadImage;

import java.util.List;

/**
 * Created by Administrator on 2017-08-24-0024.
 */

@Implement(UserImagesLoader.class)
public interface IUserImagesLoader {
    void onloadImages(Long uid,int pn,int ps);
    void onDeleteImages(String token,Long id,Long uid);

    interface LoaderUserImagesDataBinder extends DataBinder {

        void onloadImagesResponse(RespDto<List<UserLoadImage>> responce);
        void onloadImagesFailure(String msg);

        void onDeleteImagesResponse(RespDto<Boolean> responce);
        void onDeleteImagesFailure(String msg);

    }
}
