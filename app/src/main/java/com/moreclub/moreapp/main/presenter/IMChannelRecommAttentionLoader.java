package com.moreclub.moreapp.main.presenter;

import com.moreclub.common.model.annotation.Implement;
import com.moreclub.common.ui.DataBinder;
import com.moreclub.moreapp.app.RespDto;
import com.moreclub.moreapp.information.model.HeadlineSendComment;
import com.moreclub.moreapp.main.model.ChannelAttentionParam;
import com.moreclub.moreapp.main.model.ChannelAttentionResult;

/**
 * Created by Captain on 2017/7/28.
 */
@Implement(MChannelRecommAttentionLoader.class)
public interface IMChannelRecommAttentionLoader {

    void onAttentionChannel(ChannelAttentionParam param);
    void onLikeChannel(int fid,long uid,String type);
    void onSendComment(HeadlineSendComment comment);
    interface LoadMChannelRecommBinder<T> extends DataBinder {
        void onAttentionChannelResponse(RespDto<ChannelAttentionResult> responce);
        void onAttentionChannelFailure(String msg);

        void onLikeChannelResponse(RespDto<Boolean> responce);
        void onLikeChannelFailure(String msg);

        void onSendCommentResponse(RespDto<String> response);
        void onSendCommentFailure(String msg);
    }

}
