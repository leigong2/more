package com.moreclub.moreapp.main.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.log.Logger;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.main.model.MessageBase;

import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.moreclub.moreapp.main.constant.Constants.IM_RED;
import static com.moreclub.moreapp.main.constant.Constants.PUSH_RED;

/**
 * Created by Captain on 2017/5/3.
 */

public class MessageCenterItemAdapter extends RecyclerAdapter<MessageBase> {

    public MessageCenterItemAdapter(Context context, int layoutId, List<MessageBase> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, MessageBase pushMessageBase) {

        if ("push".equals(pushMessageBase.getMessageType())) {
            if (pushMessageBase.getPushType().equals("system")) {
                ((CircleImageView) holder.getView(R.id.header_img)).
                        setImageResource(R.drawable.systemsmg);
            } else if (pushMessageBase.getPushType().equals("follow")) {
                ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.myfans);
            } else if (pushMessageBase.getPushType().equals("sign")) {
                ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.signin);
            } else if (pushMessageBase.getPushType().equals("wall")) {
                ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.me);
            } else if (pushMessageBase.getPushType().equals("interaction")) {
                ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.interaction);
            }
            TextView redPoint = holder.getView(R.id.ctv_red_point);
            if (pushMessageBase.getUnReadCount() > 0) {
                redPoint.setText(pushMessageBase.getUnReadCount() + "");
                PrefsUtils.setInt(mContext,PUSH_RED,pushMessageBase.getUnReadCount());
                redPoint.setVisibility(View.VISIBLE);
            } else {
                redPoint.setVisibility(View.GONE);
            }
            ((TextView) holder.getView(R.id.title)).setText(pushMessageBase.getPushTitle());
            ((TextView) holder.getView(R.id.des)).setText(pushMessageBase.getPushDes());
            if (pushMessageBase.getPushTime() == 0) {
                ((TextView) holder.getView(R.id.time)).setText("");
            } else {
                ((TextView) holder.getView(R.id.time)).
                        setText(DateUtils.getTimestampString(new Date(pushMessageBase.getPushTime())));
            }
        } else {
            if ("im".equals(pushMessageBase.getMessageType())) {
                TextView redPoint = holder.getView(R.id.ctv_red_point);
                try {
                    if (pushMessageBase.getUnReadCount() > 0) {
                        redPoint.setText(pushMessageBase.getUnReadCount() + "");
                        redPoint.setVisibility(View.VISIBLE);
                        PrefsUtils.setInt(mContext,IM_RED,pushMessageBase.getUnReadCount());
                    } else {
                        redPoint.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    Logger.i("zune:", "e = " + e);
                }
                EMConversation item = pushMessageBase.getConversation();
                EMMessage itemMessage = item.getLastMessage();
                EMMessage fromMessage = item.getLatestMessageFromOthers();
                Log.i("zune:","uid = "+item.conversationId());
                if (item.getType() == EMConversation.EMConversationType.GroupChat) {
                    try {
                        if (itemMessage != null) {
                            Glide.with(mContext)
                                    .load(itemMessage.getStringAttribute("groupHeaderUrl"))
                                    .dontAnimate()
                                    .into(((CircleImageView) holder.getView(R.id.header_img)));
                            ((TextView) holder.getView(R.id.title)).setText(itemMessage.getStringAttribute("groupHeaderName"));
                        } else {
                            ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                            ((TextView) holder.getView(R.id.title)).setText("?");
                        }
                    } catch (HyphenateException e) {
                        ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                        ((TextView) holder.getView(R.id.title)).setText("?");
                        e.printStackTrace();
                    }
                    ((TextView) holder.getView(R.id.des)).setText(EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(itemMessage, (mContext))),
                            TextView.BufferType.SPANNABLE);
                    ((TextView) holder.getView(R.id.time)).
                            setText(DateUtils.getTimestampString(new Date(item.getLastMessage().getMsgTime())));

                } else {
                    try {
                        if (fromMessage != null) {
                            Glide.with(mContext)
                                    .load(fromMessage.getStringAttribute("headerUrl"))
                                    .dontAnimate()
                                    .placeholder(R.drawable.profilephoto_small)
                                    .into(((CircleImageView) holder.getView(R.id.header_img)));
                            String userName = fromMessage.getUserName();

                            ((TextView) holder.getView(R.id.title)).setText(fromMessage.getStringAttribute("nickName"));
                        } else {
                            String ext1 = itemMessage.getStringAttribute(item.conversationId());
                            ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                            String[] extArray = ext1.split(",");
                            if (extArray != null) {
                                if (extArray.length>=2&&extArray[1]!=null) {
                                    Glide.with(mContext)
                                            .load(extArray[1])
                                            .dontAnimate()
                                            .into(((CircleImageView) holder.getView(R.id.header_img)));
                                } else {
                                    ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                                }
                                ((TextView) holder.getView(R.id.title)).setText(extArray[0]);
                            } else {
                                ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                                ((TextView) holder.getView(R.id.title)).setText(item.getLastMessage().getUserName());
                            }
                        }

                    } catch (HyphenateException e) {
                        ((CircleImageView) holder.getView(R.id.header_img)).setImageResource(R.drawable.profilephoto_small);
                        ((TextView) holder.getView(R.id.title)).setText(item.getLastMessage().getUserName());
                        e.printStackTrace();
                    }

                    if (itemMessage.getBody() instanceof EMTextMessageBody) {
                        String message = ((EMTextMessageBody) itemMessage.getBody()).getMessage();
                        if (itemMessage.direct() == EMMessage.Direct.SEND) {
                            Spannable content = EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(itemMessage, (mContext)));
                            ((TextView) holder.getView(R.id.des)).setText(content, TextView.BufferType.SPANNABLE);
                        } else {
                            switch (message) {
                                case Constants.TITLE_SEND_INTEREST:
                                case Constants.TITLE_SEND_REPLY:
                                    ((TextView) holder.getView(R.id.des)).setText(message);
                                    return;
                                case Constants.TITLE_INTEREST:
                                    break;
                                case Constants.TITLE_REPLY:
                                    break;
                                case Constants.TITLE_ANSWER_REPLY_SELF:
                                    ((TextView) holder.getView(R.id.des)).setText(Constants.TITLE_ANSWER_REPLY);
                                    break;
                                case Constants.TITLE_ANSWER_INTEREST_SELF:
                                    ((TextView) holder.getView(R.id.des)).setText(Constants.TITLE_ANSWER_INTEREST);
                                    break;
                                default:
                                    Spannable content = EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(itemMessage, (mContext)));
                                    ((TextView) holder.getView(R.id.des)).setText(content, TextView.BufferType.SPANNABLE);
                                    break;
                            }
                        }
                    }
                    Spannable content = EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(itemMessage, (mContext)));
                    ((TextView) holder.getView(R.id.des)).setText(content, TextView.BufferType.SPANNABLE);
                    ((TextView) holder.getView(R.id.time)).
                            setText(DateUtils.getTimestampString(new Date(item.getLastMessage().getMsgTime())));
                }
            }
        }
    }
}
