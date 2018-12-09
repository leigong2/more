package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.SignInter;
import com.moreclub.moreapp.main.model.MerchantsSignInters;

/**
 * Created by Administrator on 2017-06-21.
 */

@Implement(SignInterDetailLoader.class)
public interface ISignInterDetailLoader {

    void endSignInter(String token,Integer sid);

    void onLoadSignInterDetail(String token,Integer sid);

    void onDeleteSignInterDetail(String token,Integer sid);

    void onPostSignInter(String token,SignInter inter);

    interface LoadSignInterDetailBinder extends DataBinder {

        void onEndResponse(RespDto<Boolean> body);

        void onEndFailure(String msg);

        void onLoadInterDetailResponse(RespDto<MerchantsSignInters> body);

        void onLoadInterDetailFailure(String msg);

        void onDeleteInterDetailResponse(RespDto<Object> body);

        void onDeleteInterDetailFailure(String msg);

        void onPostSignInterResponse(RespDto<Boolean> body);

        void onPostSignInterFailure(String msg);

    }
}
