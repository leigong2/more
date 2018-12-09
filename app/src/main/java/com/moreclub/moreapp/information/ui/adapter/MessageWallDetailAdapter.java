package com.moreclub.moreapp.information.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.model.MessageWallReply;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.information.ui.activity.MessageWallDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailV2Activity;
import com.moreclub.moreapp.ucenter.ui.activity.UserDetailsActivity;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017-06-06.
 */

public class MessageWallDetailAdapter extends RecyclerAdapter<MessageWallReply.RepliesBean> {

    public MessageWallDetailAdapter(Context context, int layoutId, List<MessageWallReply.RepliesBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MessageWallReply.RepliesBean messageWallReply) {
        holder.setText(R.id.tv_reply_name, messageWallReply.getUserName());
        View.OnClickListener replyListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageWallDetailActivity activity = (MessageWallDetailActivity) MessageWallDetailAdapter.this.mContext;
                activity.onClickReplyListener(messageWallReply.getUserName(),messageWallReply.getUid());
            }
        };
        View.OnClickListener civListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat compat_merchant = ActivityOptionsCompat.makeCustomAnimation(
                        mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                Intent intent_merchant = new Intent(mContext, UserDetailV2Activity.class);
                intent_merchant.putExtra("toUid", messageWallReply.getUid() + "");
                ActivityCompat.startActivity(mContext, intent_merchant, compat_merchant.toBundle());
            }
        };
        holder.setOnClickListener(R.id.civ_reply_thumb,civListener);
        holder.setOnClickListener(R.id.tv_reply_name, replyListener);
        holder.setText(R.id.tv_reply, messageWallReply.getComment());
        holder.setText(R.id.tv_reply_date, format(messageWallReply.getCreateTime()));
        Glide.with(mContext)
                .load(messageWallReply.getUserThumb())
                .dontAnimate()
                .into((CircleImageView) holder.getView(R.id.civ_reply_thumb));

    }

    private String format(Long createTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(createTime);
        String format = dateFormat.format(date);
        return format;
    }
}
