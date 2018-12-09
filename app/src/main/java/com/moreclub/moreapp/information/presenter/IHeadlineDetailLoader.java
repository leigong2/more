package com.moreclub.moreapp.information.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;

import java.util.List;

/**
 * Created by Administrator on 2017-05-17.
 */

@Implement(HeadlineDetailLoader.class)
public interface IHeadlineDetailLoader  {

    void onLoadHeadlineDetail(Integer sid,Long uid);

    void onLoadCommentList(Integer sid,Integer pageIndex,Integer pageSize,Long uid);

    void onSendComment(String token,HeadlineSendComment comment);

    interface LoadHeadlineDetailBinder extends DataBinder {

        void onGetHeadlineDetailResponse(RespDto<Headline.HeadlineDetail> response);

        void onGetHeadlineDetailFailure(String msg);

        void onGetCommentListResponse(RespDto<List<HeadlineComment>> response);

        void onGetCommentListFailure(String msg);

        void onSendCommentResponse(RespDto<String> response);

        void onSendCommentFailure(String msg);


    }
}
