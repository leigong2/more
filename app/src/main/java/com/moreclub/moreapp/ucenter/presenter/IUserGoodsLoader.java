package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.UserGoodLike;

import java.util.List;

/**
 * Created by Administrator on 2017-08-25-0025.
 */

@Implement(UserGoodsLoader.class)
public interface IUserGoodsLoader {
    void onLoadUserGoods(String token,Long uid,int pn,int ps);

    interface LoaderUserGoodsDataBinder<T> extends DataBinder {

        void onLoadGoodsResponse(RespDto<List<UserGoodLike>> respDto);
        void onLoadGoodsFailure(String msg);

    }
}
