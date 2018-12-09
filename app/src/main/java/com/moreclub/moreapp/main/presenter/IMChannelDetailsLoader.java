package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.Headline;
import com.moreclub.moreapp.information.model.HeadlineComment;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.MChannelGood;
import com.moreclub.moreapp.main.model.UGCChannel;

import java.util.List;

/**
 * Created by Administrator on 2017-07-28-0028.
 */

@Implement(MChannelDetailsLoader.class)
public interface IMChannelDetailsLoader {

    void onLoadMChannelDetails(Integer sid, Long uid);
    void onLoadUgcDetails(Integer sid, Long uid);

    void onLoadCommentList(Integer sid, Integer pageIndex, Integer pageSize,Long uid);

    void onLoadGoodList(Integer sid, Integer pageIndex, Integer pageSize);

    void onSetGood(String token, Long uid, Integer fid, String type);

    void onSetPersonGood(String token, Long uid, Integer cid);

    void onSendComment(String token, HeadlineSendComment comment);

    interface LoadMChannelDetailsBinder extends DataBinder {

        void onGetMChannelDetailsResponse(RespDto<Headline.HeadlineDetail> response);

        void onGetMChannelDetailsFailure(String msg);

        void onGetUgcDetailsResponse(RespDto<UGCChannel> response);

        void onGetUgcDetailsFailure(String msg);

        void onGetCommentListResponse(RespDto<List<HeadlineComment>> response);

        void onGetCommentListFailure(String msg);

        void onGetGoodListResponse(RespDto<MChannelGood> response);

        void onGetGoodListFailure(String msg);

        void onSetResponse(RespDto<Boolean> response);

        void onSetFailure(String msg);

        void onSetPersonGoodResponse(RespDto<Boolean> response);

        void onSetPersonGoodFailure(String msg);

        void onSendCommentResponse(RespDto<String> response);

        void onSendCommentFailure(String msg);


    }
}