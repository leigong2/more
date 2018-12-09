package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserUgc;

import java.util.List;

/**
 * Created by Administrator on 2017-08-28-0028.
 */

@Implement(MyUgcsLoader.class)
public interface IMyUgcsLoader {
    void onLoadMyUgcs(long uid,int page,int pageSize,long frientUid, int type);

    interface LoaderMyUgcsDataBinder extends DataBinder{
        void onloadMyUgcsResponse(RespDto<List<UserUgc>> responce);
        void onloadMyUgcsFailure(String msg);
    }
}
