package com.moreclub.moreapp.message.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.ui.activity.LiveActivity;
import com.moreclub.moreapp.information.ui.activity.MessageWallDetailActivity;
import com.moreclub.moreapp.message.model.MessageWall;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;

/**
 * Created by Administrator on 2017-06-07.
 */

public class MessageWallListAdapter extends RecyclerAdapter<MessageWall> {

    public MessageWallListAdapter(Context context, int layoutId, List<MessageWall> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MessageWall messageWalls) {
        MessageWall.MapBean map = messageWalls.getMap();
        String fromUserName = map.getFromUserName();
        if (fromUserName != null) {
            SpannableStringBuilder text = new SpannableStringBuilder(fromUserName + " 回复 我 的留言");
            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
            ForegroundColorSpan orangeNameSpan = new ForegroundColorSpan(Color.parseColor("#FFCC00"));
            ForegroundColorSpan orangeSpan = new ForegroundColorSpan(Color.parseColor("#FFCC00"));
            text.setSpan(orangeNameSpan, 0, fromUserName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(orangeSpan, fromUserName.length() + 4, fromUserName.length() + 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            TextView view = holder.getView(R.id.tv_reply_name);
            view.setText(text);
        }
        holder.setText(R.id.reply_time, format(messageWalls.getSendTime()));
        String content = map.getContent();
        if (content != null)
            holder.setText(R.id.tv_reply_content, content);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MoreUser.getInstance().getUid()!=0) {
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_right_in, R.anim.slide_right_out);
                    Intent intent = new Intent(mContext, MessageWallDetailActivity.class);
                    intent.putExtra(Constants.KEY_COMMENT_ID, Integer.parseInt(messageWalls.getMap().getParentId()));
                    intent.putExtra(Constants.KEY_ACT_ID, Integer.parseInt(messageWalls.getPushId()));
                    ActivityCompat.startActivity( mContext, intent, compat.toBundle());
                }
            }
        };
        holder.setOnClickListener(R.id.root_layout,listener);
    }

    private String format(long sendTime) {
        long time = System.currentTimeMillis() - sendTime;
        long min = time / 1000 / 60;
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
