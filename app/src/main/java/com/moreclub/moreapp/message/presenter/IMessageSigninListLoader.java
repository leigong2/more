package com.moreclub.moreapp.message.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.message.model.MessageSignin;

import java.util.ArrayList;

/**
 * Created by Captain on 2017/5/26.
 */
@Implement(MessageSigninListLoader.class)
public interface IMessageSigninListLoader<T> {

    void loadSigninList(String uid,String page);

    interface LoaderMessageSigninList<T> extends DataBinder{

        void onMessageSigninResponse(RespDto<ArrayList<MessageSignin>> response);
        void onMessageSigninListFailure(String msg);

    }

}
