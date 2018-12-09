package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.MChannelGood;

import java.util.List;

/**
 * Created by Administrator on 2017-08-01-0001.
 */

@Implement(MChannelCommentLoader.class)
public interface IMChannelCommentLoader {

    void onLoadMChannelDetails(Integer sid, Long uid);

    void onLoadCommentList(Integer sid, Integer pageIndex, Integer pageSize,Long uid);

    void onLoadGoodList(Integer sid, Integer pageIndex, Integer pageSize);

    void onSendComment(String token, HeadlineSendComment comment);

    void onSetGood(String token, Long uid, Integer fid, String type);

    interface LoadMChannelCommentBinder extends DataBinder {

        void onGetMChannelDetailsResponse(RespDto<Headline.HeadlineDetail> response);

        void onGetMChannelDetailsFailure(String msg);

        void onGetCommentListResponse(RespDto<List<HeadlineComment>> response);

        void onGetCommentListFailure(String msg);

        void onGetGoodListResponse(RespDto<MChannelGood> response);

        void onGetGoodListFailure(String msg);

        void onSendCommentResponse(RespDto<String> response);

        void onSendCommentFailure(String msg);

        void onSetResponse(RespDto<Boolean> response);

        void onSetFailure(String msg);



    }
}
