package com.moreclub.moreapp.message.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.common.util.PrefsUtils;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.chat.constant.Constant;
import com.moreclub.moreapp.chat.constant.DemoHelper;
import com.moreclub.moreapp.chat.ui.activity.ChatActivity;
import com.moreclub.moreapp.main.constant.Constants;
import com.moreclub.moreapp.message.model.MessageInteraction;
import com.moreclub.moreapp.message.ui.activity.MessageInteractionListActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-06-22.
 */

public class MessageInteractionListAdapter extends RecyclerAdapter<MessageInteraction> {


    public MessageInteractionListAdapter(Context context, int layoutId, List<MessageInteraction> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(final RecyclerViewHolder holder, final MessageInteraction messageInteraction) {

        final Integer status = messageInteraction.getStatus();
        // 当前状态  0 初始   1 接受  2拒绝 3 过期  4 关闭 （3,4 为活动状态） 5 邀请过期（暂未使用） 6 已阅读

        //拼座邀请
        final TextView tvReply = holder.getView(R.id.tv_reply);
        final TextView hasRead2 = holder.getView(R.id.has_read2);
        //ta感兴趣
        final TextView tvInterest = holder.getView(R.id.tv_interest);
        final TextView hasRead = holder.getView(R.id.has_read);
        if (messageInteraction.getInviteOrInter()) {
            tvReply.setVisibility(View.VISIBLE);
            tvInterest.setVisibility(View.GONE);
            hasRead.setVisibility(View.GONE);
            if (status != 0) {
                hasRead2.setVisibility(View.VISIBLE);
                TextView name = holder.getView(R.id.tv_user_name);
                name.setTextColor(Color.parseColor("#999999"));
            } else {
                hasRead2.setVisibility(View.GONE);
            }
        } else {
            tvInterest.setVisibility(View.VISIBLE);
            tvReply.setVisibility(View.GONE);
            hasRead2.setVisibility(View.GONE);
            if (status != 0) {
                hasRead.setVisibility(View.VISIBLE);
                TextView name = holder.getView(R.id.tv_user_name);
                name.setTextColor(Color.parseColor("#999999"));
            } else {
                hasRead.setVisibility(View.GONE);
            }
        }
        holder.setText(R.id.tv_user_name, messageInteraction.getNickName());
        holder.setText(R.id.tv_address, messageInteraction.getMerchantName());
        if (messageInteraction.getTimestamp() != null)
            holder.setText(R.id.time, format(messageInteraction.getTimestamp()));
        Glide.with(mContext)
                .load(messageInteraction.getThumb())
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.civ_user_thumb));
        holder.setOnClickListener(R.id.rl_root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 进入chat界面
                if (messageInteraction.getInviteOrInter()) {
                    tvReply.setVisibility(View.VISIBLE);
                    tvInterest.setVisibility(View.GONE);
                    hasRead.setVisibility(View.GONE);
                    hasRead2.setVisibility(View.VISIBLE);
                    TextView name = holder.getView(R.id.tv_user_name);
                    name.setTextColor(Color.parseColor("#999999"));
                } else {
                    tvInterest.setVisibility(View.VISIBLE);
                    tvReply.setVisibility(View.GONE);
                    hasRead2.setVisibility(View.GONE);
                    hasRead.setVisibility(View.VISIBLE);
                    TextView name = holder.getView(R.id.tv_user_name);
                    name.setTextColor(Color.parseColor("#999999"));
                }
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userId", messageInteraction.getUid() + "");
                intent.putExtra("toChatUserID", messageInteraction.getUid() + "");
                intent.putExtra("toChatNickName", messageInteraction.getNickName());
                intent.putExtra("toChatHeaderUrl", messageInteraction.getThumb());
                intent.putExtra("status", messageInteraction.getStatus());
                intent.putExtra("vid", messageInteraction.getVid());
                intent.putExtra("mid", messageInteraction.getMid());
                intent.putExtra("merchant", messageInteraction.getMerchantName());
                String tag;
                if (messageInteraction.getInviteOrInter()) {
                    tag = Constants.ANSWER;
                } else {
                    tag = Constants.ALLOW;
                }
                intent.putExtra("tag", tag);
                ActivityCompat.startActivity(mContext, intent, compat.toBundle());
            }
        });
    }

    private String format(long sendTime) {
        long time = System.currentTimeMillis() / 1000 - sendTime;
        long min = time / 60;
        if (min == 0) {
            return "刚刚";
        } else if (min < 60)
            return min + "分钟前";
        else if (min < 60 * 24)
            return min / 60 + "小时前";
        else if (min < 60 * 24 * 30)
            return min / 60 / 24 + "天前";
        else if (min < 60 * 24 * 30 * 12)
            return min / 60 / 24 / 30 + "月前";
        else
            return min / 60 / 24 / 30 / 12 + "年前";
    }
}
