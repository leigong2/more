package com.moreclub.moreapp.ucenter.presenter;

import com.moreclub.common.log.Logger;
import com.moreclub.common.ui.BasePresenter;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.ucenter.api.ApiInterface;
import com.moreclub.moreapp.ucenter.model.SecurityQuestions;
import com.moreclub.moreapp.ucenter.model.UserAnswer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017-05-15.
 */

public class QuestionLoader extends BasePresenter<IQuestionLoader.SecurityQuestionBinder> implements IQuestionLoader {
    @Override
    public void loadQuestions() {
        Callback<RespDto<SecurityQuestions>> callback = new Callback<RespDto<SecurityQuestions>>() {
            @Override
            public void onResponse(Call<RespDto<SecurityQuestions>> call, Response<RespDto<SecurityQuestions>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    getBinder().onQuestionLoadResponse(response.body());
                } else {
                    if (getBinder() != null && response != null && response.body() != null) {
                        getBinder().onQuestionLoadFailure(response.body().getErrorDesc());
                    } else {
                        getBinder().onQuestionLoadFailure(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RespDto<SecurityQuestions>> call, Throwable t) {
                getBinder().onQuestionLoadFailure(t.toString());
            }
        };
        ApiInterface.ApiFactory.createApi().onGetQuestions().enqueue(callback);
    }

    @Override
    public void postSecurityAnswer(UserAnswer answer,String token) {
        Callback<RespDto<String>> callback = new Callback<RespDto<String>>() {
            @Override
            public void onResponse(Call<RespDto<String>> call, Response<RespDto<String>> response) {
                if (response.body() != null) {
                    getBinder().onPostAnswerResponse(response.body());
                } else {
                    getBinder().onPostAnswerFailure(null);
                }
            }

            @Override
            public void onFailure(Call<RespDto<String>> call, Throwable t) {
                getBinder().onPostAnswerFailure(t.toString());
            }
        };
        ApiInterface.ApiFactory.createApi(token).onPostSecurityAnswer(answer).enqueue(callback);
    }
}
