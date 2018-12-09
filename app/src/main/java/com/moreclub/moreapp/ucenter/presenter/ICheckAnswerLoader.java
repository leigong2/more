package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.UserCheckAnswer;
import com.moreclub.moreapp.ucenter.model.UserCheckAnswerResp;

/**
 * Created by Administrator on 2017-05-22.
 */

@Implement(CheckAnswerLoader.class)
public interface ICheckAnswerLoader {

    void onCheckAnswerLoader(String open, UserCheckAnswer answer);

    void loadQuestions();

    interface LoadCheckAnswerBinder extends DataBinder {

        void onCheckAnswerResponse(RespDto<String> response);

        void onCheckAnswerFailure(String msg);

        void onQuestionLoadResponse(RespDto<SecurityQuestions> response);

        void onQuestionLoadFailure(String msg);
    }
}
