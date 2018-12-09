package com.moreclub.moreapp.information.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.moreclub.common.adapter.RecyclerAdapter;
import com.moreclub.common.adapter.RecyclerViewHolder;
import com.moreclub.moreapp.R;
import com.moreclub.moreapp.information.constant.Constants;
import com.moreclub.moreapp.information.model.MessageWall;
import com.moreclub.moreapp.information.ui.activity.MessageWallActivity;
import com.moreclub.moreapp.information.ui.activity.MessageWallDetailActivity;
import com.moreclub.moreapp.ucenter.ui.activity.UseLoginActivity;
import com.moreclub.moreapp.util.AppUtils;
import com.moreclub.moreapp.util.MoreUser;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by Administrator on 2017-06-01.
 */

public class MessageWallAdapter extends RecyclerAdapter<MessageWall> {


    private final String tag;

    public MessageWallAdapter(Context context, int layoutId, List<MessageWall> datas, String tag) {
        super(context, layoutId, datas);
        this.tag = tag;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final MessageWall messageWall) {
        LinearLayout llMessage = holder.getView(R.id.ll_message_wall);
        if (tag.equals("in")) {
            ViewGroup.LayoutParams layoutParams = llMessage.getLayoutParams();
            layoutParams.width = MATCH_PARENT;
            llMessage.setLayoutParams(layoutParams);
        }
        int[] res = {R.drawable.bg_pad_yellow, R.drawable.bg_pad_green, R.drawable.bg_pad_pink};
        Random random = new Random();
        int index = random.nextInt(3);
        llMessage.setBackgroundResource(res[index]);
        holder.setText(R.id.tv_message_wall_content, messageWall.getComment());
        holder.setText(R.id.tv_reply, "回复 " + messageWall.getReplyNum());
        holder.setText(R.id.tv_sender_name, messageWall.getUserName());
        Glide.with(mContext)
                .load(messageWall.getUserThumb())
                .dontAnimate()
                .into(((CircleImageView) holder.getView(R.id.cir_sender_icon)));
        View.OnClickListener listener = null;
        if (tag.equals("out")) {
            /**zune:活动详情里面的留言墙条目单击**/
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MoreUser.getInstance().getUid()==0) {
                        AppUtils.pushLeftActivity(mContext, UseLoginActivity.class);
                    } else {
                        ActivityOptionsCompat compat_write = ActivityOptionsCompat.makeCustomAnimation(
                                mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                        Intent intent_write = new Intent(mContext, MessageWallActivity.class);
                        intent_write.putExtra(Constants.KEY_ACT_ID, messageWall.getActivityId());
                        ActivityCompat.startActivity(mContext, intent_write, compat_write.toBundle());
                    }
                }
            };
        } else {
            /**zune:留言墙列表的条目单击**/
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityOptionsCompat compat_detail = ActivityOptionsCompat.makeCustomAnimation(
                            mContext, R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                    Intent intent_detail = new Intent(mContext, MessageWallDetailActivity.class);
                    intent_detail.putExtra(Constants.KEY_COMMENT_ID, messageWall.getCommentId());
                    intent_detail.putExtra(Constants.KEY_ACT_ID, messageWall.getActivityId());
                    ActivityCompat.startActivity(mContext, intent_detail, compat_detail.toBundle());
                }
            };
        }
        llMessage.setOnClickListener(listener);
    }
}
