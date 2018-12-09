package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.UserAnswer;

/**
 * Created by Administrator on 2017-05-15.
 */
@Implement(QuestionLoader.class)
public interface IQuestionLoader<T> {

    void loadQuestions();

    void postSecurityAnswer(UserAnswer answer, String token);

    interface SecurityQuestionBinder<T> extends DataBinder {

        void onQuestionLoadResponse(RespDto<SecurityQuestions> response);

        void onQuestionLoadFailure(String msg);

        void onPostAnswerResponse(RespDto<?> response);

        void onPostAnswerFailure(String msg);
    }
}
